package usc.etse.editoresymazmorras.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usc.etse.editoresymazmorras.dto.CrearUsuarioDTO;
import usc.etse.editoresymazmorras.dto.UsuarioDTO;
import usc.etse.editoresymazmorras.exception.NoAutenticadoException;
import usc.etse.editoresymazmorras.exception.PeticionInvalidaException;
import usc.etse.editoresymazmorras.model.Usuario;
import usc.etse.editoresymazmorras.repository.UsuarioRepository;

// Todo lo relativo a usuarios: registrar nuevos y resolver la auth por cabecera X-Usuario-Id.
@Service
public class UsuarioService {

    private static final int NOMBRE_MAX = 32;

    private final UsuarioRepository usuarios;

    public UsuarioService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    // Alta de usuario: POST /usuarios.
    @Transactional
    public UsuarioDTO anadirUsuario(CrearUsuarioDTO body) {
        if (body == null || body.nombre() == null || body.nombre().isBlank()
                || body.nombre().trim().length() > NOMBRE_MAX) {
            throw new PeticionInvalidaException("nombre obligatorio (1-32 caracteres)");
        }

        Usuario u = new Usuario();
        // La columna tiene DEFAULT gen_random_uuid(), pero JPA necesita el id ya asignado al insertar.
        u.setId(UUID.randomUUID());
        u.setNombre(body.nombre().trim());
        return UsuarioDTO.de(usuarios.save(u));
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
