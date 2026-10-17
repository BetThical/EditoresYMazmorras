package usc.etse.editoresymazmorras.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usc.etse.editoresymazmorras.service.*;
import usc.etse.editoresymazmorras.model.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {
    UsuarioService usuarioService;
    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    public record UsuarioDTO(Long id, String nombre) {
        static UsuarioController.MazmorraDTO de(Usuario usuario) {
            return new UsuarioController.UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getUpvotes(), usuario.getDownvotes(), usuario.getCreador());
        }
    }
    @PostMapping
    public ResponseEntity<Usuario> anadirUsuario(@RequestBody Usuario usuario){
        usuario=usuarioService.anadirUsuario(usuario);
        return ResponseEntity.created(URI.create("/usuarios/" + usuario.getId())).body(UsuarioController.UsuarioDTO.de(usuario));
    }
}
