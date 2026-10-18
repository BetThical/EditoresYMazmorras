package usc.etse.editoresymazmorras.exception;

// Autenticado pero sin permiso (no es el creador) -> 403.
public class ProhibidoException extends RuntimeException {

    public ProhibidoException(String mensaje) {
        super(mensaje);
    }
}
