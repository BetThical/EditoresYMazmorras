package usc.etse.editoresymazmorras.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usc.etse.editoresymazmorras.model.Cofre;
import usc.etse.editoresymazmorras.model.ElementoMazmorra;
import usc.etse.editoresymazmorras.model.ElementoMazmorraId;
import usc.etse.editoresymazmorras.model.Mazmorra;
import usc.etse.editoresymazmorras.model.TipoElemento;
import usc.etse.editoresymazmorras.model.Voto;
import usc.etse.editoresymazmorras.model.VotoId;
import usc.etse.editoresymazmorras.repository.CofreRepository;
import usc.etse.editoresymazmorras.repository.ElementoMazmorraRepository;
import usc.etse.editoresymazmorras.repository.MazmorraRepository;
import usc.etse.editoresymazmorras.repository.UsuarioRepository;
import usc.etse.editoresymazmorras.repository.VotoRepository;

@RestController
@RequestMapping("/mazmorras")
public class MazmorraController {
    // API del editor: crear mazmorra, ver/guardar su mapa y votar.
    // Auth casera por cabecera X-Usuario-Id; GET detalle es público, el resto exige usuario.

    private final MazmorraRepository mazmorras;
    private final UsuarioRepository usuarios;
    private final ElementoMazmorraRepository elementos;
    private final CofreRepository cofres;
    private final VotoRepository votos;

    public MazmorraController(MazmorraRepository mazmorras, UsuarioRepository usuarios,
            ElementoMazmorraRepository elementos, CofreRepository cofres, VotoRepository votos) {
        this.mazmorras = mazmorras;
        this.usuarios = usuarios;
        this.elementos = elementos;
        this.cofres = cofres;
        this.votos = votos;
    }

    public record CrearMazmorraDTO(String nombre) {
    }

    public record MazmorraDTO(Long id, String nombre, long upvotes, long downvotes, UUID creador) {
        static MazmorraDTO de(Mazmorra m) {
            return new MazmorraDTO(m.getId(), m.getNombre(), m.getUpvotes(), m.getDownvotes(), m.getCreador());
        }
    }

    // Una casilla tal como la ve el frontend (objetos solo si es COFRE).
    public record CeldaDTO(int x, int y, int planta, TipoElemento tipo, List<String> objetos) {
    }

    // Detalle completo: cabecera + mapa entero para pintar el editor.
    public record MazmorraDetalleDTO(Long id, String nombre, long upvotes, long downvotes, UUID creador,
            List<CeldaDTO> celdas) {
    }

    // Payload de guardado del editor: reemplaza el mapa entero (bulk, más simple que celda a celda).
    public record GuardarMapaDTO(List<CeldaDTO> celdas) {
    }

    // Votar es crear/actualizar "mi voto", no editar el contador a mano.
    public record VotarDTO(Short valor) {
    }

    // Alta de mazmorra vacía (sin mapa): el mapa se guarda después con PUT /{id}/mapa.
    @PostMapping
    public ResponseEntity<?> crear(
            @RequestHeader(value = "X-Usuario-Id", required = false) UUID usuarioId,
            @RequestBody(required = false) CrearMazmorraDTO body) {

        if (usuarioId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Falta cabecera X-Usuario-Id"));
        }
        if (!usuarios.existsById(usuarioId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario no autenticado"));
        }
        if (body == null || body.nombre() == null || body.nombre().isBlank() || body.nombre().trim().length() > 64) {
            return ResponseEntity.badRequest().body(Map.of("error", "nombre obligatorio (1-64 caracteres)"));
        }

        Mazmorra m = new Mazmorra();
        m.setNombre(body.nombre().trim());
        m.setCreador(usuarioId);
        m = mazmorras.save(m);

        return ResponseEntity.created(URI.create("/mazmorras/" + m.getId())).body(MazmorraDTO.de(m));
    }

    // Devuelve la mazmorra con todas sus casillas (el editor la carga de una vez).
    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        var m = mazmorras.findById(id);
        if (m.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(aDetalle(m.get()));
    }

    // Reemplaza el mapa entero: borra casillas/cofres viejos y guarda los nuevos en una transacción.
    @PutMapping("/{id}/mapa")
    @Transactional
    public ResponseEntity<?> guardarMapa(
            @PathVariable Long id,
            @RequestHeader(value = "X-Usuario-Id", required = false) UUID usuarioId,
            @RequestBody(required = false) GuardarMapaDTO body) {

        var m = mazmorras.findById(id);
        if (m.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Solo el creador puede editar (el frontend ocultará el editor al resto).
        if (usuarioId == null || !usuarios.existsById(usuarioId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario no autenticado"));
        }
        if (!m.get().getCreador().equals(usuarioId)) {
            return ResponseEntity.status(403).body(Map.of("error", "Solo el creador puede editar"));
        }
        if (body == null || body.celdas() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "celdas obligatorio"));
        }

        // Validación: sin coordenadas duplicadas ni tipos nulos.
        Set<String> vistas = new HashSet<>();
        for (var c : body.celdas()) {
            if (c == null || c.tipo() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Cada celda necesita tipo"));
            }
            String clave = c.x() + "," + c.y() + "," + c.planta();
            if (!vistas.add(clave)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Celda duplicada: " + clave));
            }
            // Coherencia cofre<->objetos: solo las casillas COFRE llevan botín.
            boolean esCofre = c.tipo() == TipoElemento.COFRE;
            boolean conObjetos = c.objetos() != null && !c.objetos().isEmpty();
            if (!esCofre && conObjetos) {
                return ResponseEntity.badRequest().body(Map.of("error", "Solo COFRE admite objetos: " + clave));
            }
            if (conObjetos && c.objetos().stream().anyMatch(o -> o == null || o.isBlank() || o.length() > 64)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Objetos de 1-64 caracteres: " + clave));
            }
        }

        // Sustitución completa: primero los cofres (FK a casilla), luego las casillas.
        cofres.deleteByIdMazmorra(id);
        elementos.deleteByIdMazmorra(id);

        List<ElementoMazmorra> nuevas = new ArrayList<>();
        List<Cofre> nuevosCofres = new ArrayList<>();
        for (var c : body.celdas()) {
            var pk = new ElementoMazmorraId(id, c.x(), c.y(), c.planta());
            var e = new ElementoMazmorra();
            e.setId(pk);
            e.setTipo(c.tipo());
            nuevas.add(e);
            if (c.tipo() == TipoElemento.COFRE) {
                var cofre = new Cofre();
                cofre.setId(pk);
                cofre.setObjetos(c.objetos() == null ? new String[0] : c.objetos().toArray(String[]::new));
                nuevosCofres.add(cofre);
            }
        }
        elementos.saveAll(nuevas);
        cofres.saveAll(nuevosCofres);

        return ResponseEntity.ok(aDetalle(m.get()));
    }

    // Emite o cambia mi voto (+1/-1): idempotente en efecto, un voto por usuario.
    @PostMapping("/{id}/votos")
    @Transactional
    public ResponseEntity<?> votar(
            @PathVariable Long id,
            @RequestHeader(value = "X-Usuario-Id", required = false) UUID usuarioId,
            @RequestBody(required = false) VotarDTO body) {

        var m = mazmorras.findById(id);
        if (m.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (usuarioId == null || !usuarios.existsById(usuarioId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario no autenticado"));
        }
        if (body == null || (body.valor() != 1 && body.valor() != -1)) {
            return ResponseEntity.badRequest().body(Map.of("error", "valor debe ser 1 o -1"));
        }

        // Upsert de mi voto + recuento desde la tabla voto (fuente de verdad).
        var voto = new Voto();
        voto.setId(new VotoId(id, usuarioId));
        voto.setValor(body.valor());
        votos.save(voto);
        Mazmorra maz = m.get();
        maz.setUpvotes(votos.countByIdMazmorraAndValor(id, (short) 1));
        maz.setDownvotes(votos.countByIdMazmorraAndValor(id, (short) -1));
        mazmorras.save(maz);

        return ResponseEntity.ok(MazmorraDTO.de(maz));
    }

    // Junta cabecera + casillas + botines en un solo DTO para el frontend.
    private MazmorraDetalleDTO aDetalle(Mazmorra m) {
        Map<ElementoMazmorraId, List<String>> botin = new HashMap<>();
        for (var c : cofres.findByIdMazmorra(m.getId())) {
            botin.put(c.getId(), List.of(c.getObjetos()));
        }
        List<CeldaDTO> celdas = elementos.findByIdMazmorra(m.getId()).stream()
                .map(e -> new CeldaDTO(e.getId().getPosx(), e.getId().getPosy(), e.getId().getPlanta(), e.getTipo(),
                        botin.getOrDefault(e.getId(), List.of())))
                .toList();
        return new MazmorraDetalleDTO(m.getId(), m.getNombre(), m.getUpvotes(), m.getDownvotes(), m.getCreador(),
                celdas);
    }
}
