package es.upm.sos.vinos.exception;

public class VinoNotFoundException extends RuntimeException {
    public VinoNotFoundException(Integer id){
        super("Vino con id "+id+" no encontrado.");
    }
}
