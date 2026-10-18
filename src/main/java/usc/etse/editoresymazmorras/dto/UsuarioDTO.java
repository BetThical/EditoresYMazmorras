package usc.etse.editoresymazmorras.dto;

import java.util.UUID;
import usc.etse.editoresymazmorras.model.Usuario;

// Usuario tal como lo ve el frontend (nunca la entidad ni el id de escritura del cliente).
public record UsuarioDTO(UUID id, String nombre) {

    public static UsuarioDTO de(Usuario u) {
        return new UsuarioDTO(u.getId(), u.getNombre());
    }
}
