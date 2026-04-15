package es.upm.sos.vinos.model;


import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioVinoId {
    @NotNull
    private int clienteId;
    private int vinoId;
}
