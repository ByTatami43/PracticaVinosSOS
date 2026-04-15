package es.upm.sos.vinos.assembler;

import es.upm.sos.vinos.controller.ClientesController;
import es.upm.sos.vinos.model.Cliente;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ClientesModelAssembler extends RepresentationModelAssemblerSupport<Cliente, Cliente> {

    public ClientesModelAssembler() {
        super(ClientesController.class, Cliente.class);
    }

    @Override
    public Cliente toModel(Cliente entity) {
        entity.add(linkTo(methodOn(ClientesController.class)
                .getCliente(entity.getId())).withSelfRel());
        return entity;
    }
}