package usc.etse.editoresymazmorras.exception;

// Falta la cabecera X-Usuario-Id o el usuario no existe -> 401.
public class NoAutenticadoException extends RuntimeException {

    public NoAutenticadoException(String mensaje) {
        super(mensaje);
    }
}
