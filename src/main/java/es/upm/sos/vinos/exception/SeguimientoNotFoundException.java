package es.upm.sos.vinos.exception;

public class SeguimientoNotFoundException extends RuntimeException {
    public SeguimientoNotFoundException(Integer id){
        super("Solicitud de seguimiento con id "+id+" no encontrado.");
    }
    public SeguimientoNotFoundException(Integer id,Integer id2){
        super("Solicitud de seguimiento entre "+id+ "y " + id2 + " no encontrado.");
    }
}
