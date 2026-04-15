package es.upm.sos.vinos.exception;

public class ClienteExistsException extends RuntimeException {
    public ClienteExistsException(String nombre) {
        super("Cliente con nombre: "+nombre+" ya existe.");
    }
}
