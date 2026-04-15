package es.upm.sos.vinos.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "vinos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vino extends RepresentationModel<Vino> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String nombre;
    @NotNull
    private String tipoVino;
    @NotNull
    private String uva;
    @NotNull
    private String origen;
    @NotNull
    private String bodega;
}
