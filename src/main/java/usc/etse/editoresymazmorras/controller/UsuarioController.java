package usc.etse.editoresymazmorras.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usc.etse.editoresymazmorras.dto.CrearUsuarioDTO;
import usc.etse.editoresymazmorras.dto.UsuarioDTO;
import usc.etse.editoresymazmorras.exception.NoEncontradoException;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<UsuarioDTO> eliminarUsuario(@PathVariable String id) {
        try {
            usuarios.eliminarUsuario(UUID.fromString(id));
        }
        catch(NoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
