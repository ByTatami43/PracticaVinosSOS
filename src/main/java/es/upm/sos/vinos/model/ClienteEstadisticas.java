package es.upm.sos.vinos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteEstadisticas extends RepresentationModel<ClienteEstadisticas> {
    private Integer clienteId;
    private Double mediaPuntuacion;
    private Long totalVinos;
}
