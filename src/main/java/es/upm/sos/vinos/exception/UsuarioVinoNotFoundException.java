package es.upm.sos.vinos.exception;

public class UsuarioVinoNotFoundException extends RuntimeException {
    public UsuarioVinoNotFoundException(Integer id,Integer idVino){
        super("Relacion entre usuario "+id+ "y vino: "+idVino+" no encontrado.");
    }
}
