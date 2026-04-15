package es.upm.sos.vinos.assembler;

import es.upm.sos.vinos.controller.VinosController;
import es.upm.sos.vinos.model.Vino;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class VinoModelAssembler extends RepresentationModelAssemblerSupport<Vino, Vino> {

    public VinoModelAssembler() {
        super(VinosController.class, Vino.class);
    }

    @Override
    public Vino toModel(Vino entity) {
        entity.add(linkTo(methodOn(VinosController.class)
                .getVino(entity.getId())).withSelfRel());
        return entity;
    }
}