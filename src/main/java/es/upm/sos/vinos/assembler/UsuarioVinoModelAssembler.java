package es.upm.sos.vinos.assembler;

import es.upm.sos.vinos.controller.ClientesController;
import es.upm.sos.vinos.model.UsuarioVino;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioVinoModelAssembler extends RepresentationModelAssemblerSupport<UsuarioVino, UsuarioVino> {

    public UsuarioVinoModelAssembler() {
        super(ClientesController.class, UsuarioVino.class);
    }

    @Override
    public UsuarioVino toModel(UsuarioVino entity) {
        entity.add(linkTo(methodOn(ClientesController.class)
                .getVinoDeCliente(
                        entity.getCliente().getId(),
                        entity.getVino().getId()))
                .withSelfRel());
        return entity;
    }
}