package usc.etse.editoresymazmorras.controller;

import java.net.URI;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usc.etse.editoresymazmorras.model.Mazmorra;
import usc.etse.editoresymazmorras.repository.MazmorraRepository;
import usc.etse.editoresymazmorras.repository.UsuarioRepository;

@RestController
@RequestMapping("/mazmorras")
public class MazmorraController {

    private final MazmorraRepository mazmorras;
    private final UsuarioRepository usuarios;

    public MazmorraController(MazmorraRepository mazmorras, UsuarioRepository usuarios) {
        this.mazmorras = mazmorras;
        this.usuarios = usuarios;
    }

    public record CrearMazmorraDTO(String nombre) {
    }

    public record MazmorraDTO(Long id, String nombre, long upvotes, long downvotes, UUID creador) {
        static MazmorraDTO de(Mazmorra m) {
            return new MazmorraDTO(m.getId(), m.getNombre(), m.getUpvotes(), m.getDownvotes(), m.getCreador());
        }
    }

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
}
