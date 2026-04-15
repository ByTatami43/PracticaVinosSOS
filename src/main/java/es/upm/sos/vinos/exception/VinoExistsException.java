package es.upm.sos.vinos.exception;

public class VinoExistsException extends RuntimeException {
    public VinoExistsException(String nombre) {
        super("Vino con nombre: "+nombre+" ya existe.");
    }
}
