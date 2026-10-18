package usc.etse.editoresymazmorras.exception;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Traduce las excepciones de dominio a códigos HTTP, así los controllers solo "hablan" de negocio.
// Todas responden con el mismo cuerpo {"error": "..."} que ya usaba la API.
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NoEncontradoException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(NoEncontradoException e) {
        return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(NoAutenticadoException.class)
    public ResponseEntity<Map<String, String>> noAutenticado(NoAutenticadoException e) {
        return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(ProhibidoException.class)
    public ResponseEntity<Map<String, String>> prohibido(ProhibidoException e) {
        return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(PeticionInvalidaException.class)
    public ResponseEntity<Map<String, String>> peticionInvalida(PeticionInvalidaException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}
