package usc.etse.editoresymazmorras.controller;

import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usc.etse.editoresymazmorras.dto.CrearMazmorraDTO;
import usc.etse.editoresymazmorras.dto.GuardarMapaDTO;
import usc.etse.editoresymazmorras.dto.MazmorraDTO;
import usc.etse.editoresymazmorras.dto.MazmorraDetalleDTO;
import usc.etse.editoresymazmorras.dto.VotarDTO;
import usc.etse.editoresymazmorras.service.MazmorraService;

// Capa HTTP: recibe DTOs, delega en el servicio y devuelve DTOs.
// Errores (401/403/404/400) los pone ApiExceptionHandler a partir de las excepciones del servicio.
@RestController
@RequestMapping("/mazmorras")
public class MazmorraController {

    private final MazmorraService mazmorras;

    public MazmorraController(MazmorraService mazmorras) {
        this.mazmorras = mazmorras;
    }

    @PostMapping
    public ResponseEntity<MazmorraDTO> crear(
            @RequestHeader(value = "X-Usuario-Id", required = false) UUID usuarioId,
            @RequestBody(required = false) CrearMazmorraDTO body) {
        MazmorraDTO creada = mazmorras.crear(usuarioId, body);
        return ResponseEntity.created(URI.create("/mazmorras/" + creada.id())).body(creada);
    }

    @GetMapping("/{id}")
    public MazmorraDetalleDTO detalle(@PathVariable Long id) {
        return mazmorras.detalle(id);
    }

    @PutMapping("/{id}/mapa")
    public MazmorraDetalleDTO guardarMapa(
            @PathVariable Long id,
            @RequestHeader(value = "X-Usuario-Id", required = false) UUID usuarioId,
            @RequestBody(required = false) GuardarMapaDTO body) {
        return mazmorras.guardarMapa(id, usuarioId, body);
    }

    @PostMapping("/{id}/votos")
    public MazmorraDTO votar(
            @PathVariable Long id,
            @RequestHeader(value = "X-Usuario-Id", required = false) UUID usuarioId,
            @RequestBody(required = false) VotarDTO body) {
        return mazmorras.votar(id, usuarioId, body);
    }
}
