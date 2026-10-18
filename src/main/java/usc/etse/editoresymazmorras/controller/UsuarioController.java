package usc.etse.editoresymazmorras.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usc.etse.editoresymazmorras.dto.CrearUsuarioDTO;
import usc.etse.editoresymazmorras.dto.UsuarioDTO;
import usc.etse.editoresymazmorras.service.UsuarioService;

// Capa HTTP de usuarios: solo recibe el DTO, delega en el servicio y devuelve un DTO.
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarios;

    public UsuarioController(UsuarioService usuarios) {
        this.usuarios = usuarios;
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> anadirUsuario(@RequestBody(required = false) CrearUsuarioDTO body) {
        UsuarioDTO creado = usuarios.anadirUsuario(body);
        return ResponseEntity.created(URI.create("/usuarios/" + creado.id())).body(creado);
    }
}
