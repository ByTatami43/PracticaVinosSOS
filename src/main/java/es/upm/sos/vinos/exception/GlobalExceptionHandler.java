package es.upm.sos.vinos.exception;


import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ClienteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorMessage userNotFoundHandler(ClienteNotFoundException ex){
        return new ErrorMessage(ex.getMessage());
    }
    @ExceptionHandler(VinoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorMessage vinoNotFoundHandler(VinoNotFoundException ex){
        return new ErrorMessage(ex.getMessage());
    }
    @ExceptionHandler(UsuarioVinoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorMessage usuarioVinoNotFoundHandler(UsuarioVinoNotFoundException ex){
        return new ErrorMessage(ex.getMessage());
    }
    @ExceptionHandler(SeguimientoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorMessage seguimientoNotFoundHandler(SeguimientoNotFoundException ex){
        return new ErrorMessage(ex.getMessage());
    }
    @ExceptionHandler(UsuarioMenorDeEdadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorMessage usuarioMenorDeEdadHandler(UsuarioMenorDeEdadException ex){
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(ClienteExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ErrorMessage clienteExistsHandler(ClienteExistsException ex){
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) //Ejecuta este metodo si se envia un JSON no valido
    @ResponseStatus(HttpStatus.BAD_REQUEST) //Asegura que el cliente reciba codigo 400 BAD_REQUEST
    public ErrorMessage handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult()//Accede al informe de errores que genera Spring
                .getAllErrors().forEach( //Recorre todos los errores encontrados
                        (error)->{
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ErrorMessage(errors.toString());
    }

}
