package es.upm.sos.vinos.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@Table(name = "usuario_vino")
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class UsuarioVino extends RepresentationModel<UsuarioVino> {
    @EmbeddedId
    private UsuarioVinoId id;

    @ManyToOne
    @MapsId("vinoId")
    @JoinColumn(name = "vino_id")
    private Vino vino;

    @ManyToOne
    @MapsId("clienteId")
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @NotNull
    private Integer puntuacion;

    @NotNull
    private LocalDate fechaAdicion;

}
