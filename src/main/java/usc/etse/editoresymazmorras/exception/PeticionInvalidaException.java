package usc.etse.editoresymazmorras.exception;

// Payload inválido (nombre, celdas, valor de voto...) -> 400.
public class PeticionInvalidaException extends RuntimeException {

    public PeticionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
