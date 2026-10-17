package usc.etse.editoresymazmorras.service;

import usc.etse.editoresymazmorras.model.Usuario;
import usc.etse.editoresymazmorras.repository.*;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    public Usuario anadirUsuario(@NonNull Usuario usuario){
        return usuarioRepository.save(usuario);
    }
}
