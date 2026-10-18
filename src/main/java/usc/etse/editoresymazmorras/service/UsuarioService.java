package usc.etse.editoresymazmorras.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import usc.etse.editoresymazmorras.exception.NoAutenticadoException;
import usc.etse.editoresymazmorras.repository.UsuarioRepository;

// Todo lo relativo a usuarios. Ahora mismo solo resuelve la auth por cabecera X-Usuario-Id,
// que antes estaba copiada en cada método del controller.
@Service
public class UsuarioService {

    private final UsuarioRepository usuarios;

    public UsuarioService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    // Valida la identidad y devuelve el id; si no vale, lanza 401 (lo traduce ApiExceptionHandler).
    public UUID exigirUsuario(UUID usuarioId) {
        if (usuarioId == null) {
            throw new NoAutenticadoException("Falta cabecera X-Usuario-Id");
        }
        if (!usuarios.existsById(usuarioId)) {
            throw new NoAutenticadoException("Usuario no autenticado");
        }
        return usuarioId;
    }
}
