package es.upm.sos.vinos.controller;


import es.upm.sos.vinos.assembler.ClientesModelAssembler;
import es.upm.sos.vinos.exception.ClienteExistsException;
import es.upm.sos.vinos.exception.ClienteNotFoundException;
import es.upm.sos.vinos.model.Cliente;
import es.upm.sos.vinos.model.ClienteEstadisticas;
import es.upm.sos.vinos.model.ClienteRecomendaciones;
import es.upm.sos.vinos.service.ClientesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/clientes")
@AllArgsConstructor
public class ClientesController {
    private final ClientesService clientesService;
    private PagedResourcesAssembler<Cliente> pagedResourcesAssembler;
    private ClientesModelAssembler clientesModelAssembler;

    @PostMapping
    public ResponseEntity<Void> crearCliente(@Valid @RequestBody Cliente nuevoCliente){
        if(!clientesService.existeCliente(nuevoCliente.getNombre())){
            Cliente cliente = clientesService.crearCliente(nuevoCliente);
            return ResponseEntity.created(linkTo(ClientesController.class).slash(cliente.getId()).toUri()).build();
        }
        throw new ClienteExistsException(nuevoCliente.getNombre());
    }

    @GetMapping(value = "/{id}",produces = {"application/json"})
    public ResponseEntity<Cliente> getCliente(@PathVariable Integer id){
        Cliente cliente = clientesService.buscarPorId(id).orElseThrow(()->new ClienteNotFoundException(id));
        cliente.add(linkTo(methodOn(ClientesController.class).getCliente(id)).withSelfRel());
        return ResponseEntity.ok(cliente);
    }
    @GetMapping(produces = {"application/json"})
    public ResponseEntity<PagedModel<Cliente>> getClientes(@RequestParam(defaultValue = "", required = false) String contains,
                                                           @RequestParam(defaultValue = "0", required = false) int page,
                                                           @RequestParam(defaultValue = "2", required = false) int size){
        Page<Cliente> clientes = clientesService.buscarClientes(contains,page,size);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(clientes,clientesModelAssembler));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> actualizarCliente(@PathVariable Integer id, @Valid @RequestBody Cliente clienteActualizado) {
        clientesService.actualizarCliente(id, clienteActualizado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Integer id){
        clientesService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ClienteRecomendaciones> getRecomendaciones(@PathVariable Integer id){
        return ResponseEntity.ok(clientesService.obtenerRecomendaciones(id));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ClienteEstadisticas> getEstadisticas(@PathVariable Integer id,
                                                               @RequestParam(defaultValue = "", required = false) String bodega,
                                                               @RequestParam(defaultValue = "", required = false) String origen,
                                                               @RequestParam(defaultValue = "", required = false) String tipo,
                                                               @RequestParam(defaultValue = "", required = false) Integer anyo,
                                                               @RequestParam(defaultValue = "", required = false) String uva,
                                                               @RequestParam(defaultValue = "", required = false) LocalDate desde,
                                                               @RequestParam(defaultValue = "", required = false) LocalDate hasta){

        return ResponseEntity.ok(clientesService.obtenerEstadisticas(id,bodega,origen,tipo,anyo,uva,desde,hasta));
    }



}
