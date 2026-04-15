package es.upm.sos.vinos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClienteRecomendaciones extends RepresentationModel<ClienteRecomendaciones> {
    private Cliente cliente;
    private List<UsuarioVino> ultimosVinos;       // 5 últimos añadidos
    private List<UsuarioVino> mejoresVinos;        // 5 con mayor puntuación propia
    private List<UsuarioVino> mejoresVinosAmigos;  // 5 con mayor puntuación de amigos
}
