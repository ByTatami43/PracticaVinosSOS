package es.upm.sos.vinos.assembler;

import es.upm.sos.vinos.controller.ClientesController;
import es.upm.sos.vinos.model.Seguimiento;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class SeguimientoModelAssembler extends RepresentationModelAssemblerSupport<Seguimiento, Seguimiento> {

    public SeguimientoModelAssembler() {
        super(ClientesController.class, Seguimiento.class);
    }

    @Override
    public Seguimiento toModel(Seguimiento entity) {
        entity.add(linkTo(methodOn(ClientesController.class)
                .getSeguimiento(
                        entity.getSolicitante().getId(),
                        entity.getSeguido().getId()))
                .withSelfRel());
        return entity;
    }
}