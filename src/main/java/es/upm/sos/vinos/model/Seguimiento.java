package es.upm.sos.vinos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;


@Data
@Table(name = "seguimiento")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Seguimiento extends RepresentationModel<Seguimiento> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "usuario_solicitante_id")
    private Cliente solicitante;

    @ManyToOne
    @JoinColumn(name = "usuario_seguido_id")
    private Cliente seguido;

    @NotNull
    private EstadoSeguimiento estado;
}
