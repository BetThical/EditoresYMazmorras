package usc.etse.editoresymazmorras.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usc.etse.editoresymazmorras.dto.CeldaDTO;
import usc.etse.editoresymazmorras.dto.CrearMazmorraDTO;
import usc.etse.editoresymazmorras.dto.GuardarMapaDTO;
import usc.etse.editoresymazmorras.dto.MazmorraDTO;
import usc.etse.editoresymazmorras.dto.MazmorraDetalleDTO;
import usc.etse.editoresymazmorras.dto.VotarDTO;
import usc.etse.editoresymazmorras.exception.NoEncontradoException;
import usc.etse.editoresymazmorras.exception.PeticionInvalidaException;
import usc.etse.editoresymazmorras.exception.ProhibidoException;
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
import usc.etse.editoresymazmorras.repository.VotoRepository;

// Reglas de negocio de la mazmorra: crear, leer/guardar el mapa y votar.
// El controller solo traduce HTTP -> estas llamadas.
@Service
public class MazmorraService {

    private static final int NOMBRE_MAX = 64;

    private final MazmorraRepository mazmorras;
    private final ElementoMazmorraRepository elementos;
    private final CofreRepository cofres;
    private final VotoRepository votos;
    private final UsuarioService usuarios;

    public MazmorraService(MazmorraRepository mazmorras, ElementoMazmorraRepository elementos,
            CofreRepository cofres, VotoRepository votos, UsuarioService usuarios) {
        this.mazmorras = mazmorras;
        this.elementos = elementos;
        this.cofres = cofres;
        this.votos = votos;
        this.usuarios = usuarios;
    }

    // Alta de mazmorra vacía (sin mapa): el mapa se guarda después con guardarMapa.
    @Transactional
    public MazmorraDTO crear(UUID usuarioId, CrearMazmorraDTO body) {
        UUID creador = usuarios.exigirUsuario(usuarioId);
        if (body == null || body.nombre() == null || body.nombre().isBlank()
                || body.nombre().trim().length() > NOMBRE_MAX) {
            throw new PeticionInvalidaException("nombre obligatorio (1-64 caracteres)");
        }

        Mazmorra m = new Mazmorra();
        m.setNombre(body.nombre().trim());
        m.setCreador(creador);
        return MazmorraDTO.de(mazmorras.save(m));
    }

    // Mazmorra con todas sus casillas (el editor la carga de una vez).
    @Transactional(readOnly = true)
    public MazmorraDetalleDTO detalle(Long id) {
        return aDetalle(buscar(id));
    }

    // Reemplaza el mapa entero: borra casillas/cofres viejos y guarda los nuevos en una transacción.
    @Transactional
    public MazmorraDetalleDTO guardarMapa(Long id, UUID usuarioId, GuardarMapaDTO body) {
        Mazmorra m = buscar(id);
        UUID usuario = usuarios.exigirUsuario(usuarioId);
        // Solo el creador puede editar (el frontend ocultará el editor al resto).
        if (!m.getCreador().equals(usuario)) {
            throw new ProhibidoException("Solo el creador puede editar");
        }
        if (body == null || body.celdas() == null) {
            throw new PeticionInvalidaException("celdas obligatorio");
        }
        validarCeldas(body.celdas());

        // Sustitución completa. El flush fuerza que los DELETE lleguen a la BD antes de los INSERT,
        // si no Hibernate reordena y chocaría con la PK (mismo x,y,planta).
        cofres.deleteByIdMazmorra(id);
        cofres.flush();
        elementos.deleteByIdMazmorra(id);
        elementos.flush();

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
        // Casillas antes que cofres: la FK de cofre apunta a elementomazmorra.
        elementos.saveAll(nuevas);
        cofres.saveAll(nuevosCofres);

        return aDetalle(m);
    }

    // Emite o cambia mi voto (+1/-1): idempotente en efecto, un voto por usuario.
    @Transactional
    public MazmorraDTO votar(Long id, UUID usuarioId, VotarDTO body) {
        Mazmorra m = buscar(id);
        UUID usuario = usuarios.exigirUsuario(usuarioId);
        if (body == null || body.valor() == null || (body.valor() != 1 && body.valor() != -1)) {
            throw new PeticionInvalidaException("valor debe ser 1 o -1");
        }

        // Upsert de mi voto + recuento desde la tabla voto (fuente de verdad).
        var voto = new Voto();
        voto.setId(new VotoId(id, usuario));
        voto.setValor(body.valor());
        votos.save(voto);
        m.setUpvotes(votos.countByIdMazmorraAndValor(id, (short) 1));
        m.setDownvotes(votos.countByIdMazmorraAndValor(id, (short) -1));
        return MazmorraDTO.de(mazmorras.save(m));
    }

    private Mazmorra buscar(Long id) {
        return mazmorras.findById(id)
                .orElseThrow(() -> new NoEncontradoException("Mazmorra no encontrada"));
    }

    // Validación del mapa: sin duplicados, tipos obligatorios y coherencia cofre <-> objetos.
    private void validarCeldas(List<CeldaDTO> celdas) {
        Set<String> vistas = new HashSet<>();
        for (var c : celdas) {
            if (c == null || c.tipo() == null) {
                throw new PeticionInvalidaException("Cada celda necesita tipo");
            }
            String clave = c.x() + "," + c.y() + "," + c.planta();
            if (!vistas.add(clave)) {
                throw new PeticionInvalidaException("Celda duplicada: " + clave);
            }
            // Coherencia cofre<->objetos: solo las casillas COFRE llevan botín.
            boolean esCofre = c.tipo() == TipoElemento.COFRE;
            boolean conObjetos = c.objetos() != null && !c.objetos().isEmpty();
            if (!esCofre && conObjetos) {
                throw new PeticionInvalidaException("Solo COFRE admite objetos: " + clave);
            }
            if (conObjetos && c.objetos().stream().anyMatch(o -> o == null || o.isBlank() || o.length() > NOMBRE_MAX)) {
                throw new PeticionInvalidaException("Objetos de 1-64 caracteres: " + clave);
            }
        }
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
