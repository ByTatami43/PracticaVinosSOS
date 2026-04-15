package es.upm.sos.vinos.exception;

public class UsuarioMenorDeEdadException extends RuntimeException {
    public UsuarioMenorDeEdadException() {
        super("Usuario menor de edad.");
    }
}
