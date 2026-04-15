package es.upm.sos.vinos.controller;


import es.upm.sos.vinos.assembler.ClientesModelAssembler;
import es.upm.sos.vinos.assembler.SeguimientoModelAssembler;
import es.upm.sos.vinos.assembler.UsuarioVinoModelAssembler;
import es.upm.sos.vinos.exception.ClienteExistsException;
import es.upm.sos.vinos.exception.ClienteNotFoundException;
import es.upm.sos.vinos.exception.VinoNotFoundException;
import es.upm.sos.vinos.model.*;
import es.upm.sos.vinos.service.ClientesService;
import es.upm.sos.vinos.service.SeguimientoService;
import es.upm.sos.vinos.service.UsuarioVinoService;
import es.upm.sos.vinos.service.VinoService;
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
    private final UsuarioVinoService usuarioVinoService;
    private final VinoService vinoService;
    private PagedResourcesAssembler<Cliente> pagedResourcesAssembler;
    private ClientesModelAssembler clientesModelAssembler;
    private PagedResourcesAssembler<UsuarioVino> usuarioVinoPagedResourcesAssembler;

    private UsuarioVinoModelAssembler usuarioVinoModelAssembler;

    private final SeguimientoService seguimientoService;
    private final SeguimientoModelAssembler seguimientoModelAssembler;
    private PagedResourcesAssembler<Seguimiento> seguimientoPagedAssembler;

    @PostMapping
    public ResponseEntity<Void> crearCliente(@Valid @RequestBody Cliente nuevoCliente) {
        if (!clientesService.existeCliente(nuevoCliente.getNombre())) {
            Cliente cliente = clientesService.crearCliente(nuevoCliente);
            return ResponseEntity.created(linkTo(ClientesController.class).slash(cliente.getId()).toUri()).build();
        }
        throw new ClienteExistsException(nuevoCliente.getNombre());
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<Cliente> getCliente(@PathVariable Integer id) {
        Cliente cliente = clientesService.buscarPorId(id).orElseThrow(() -> new ClienteNotFoundException(id));
        cliente.add(linkTo(methodOn(ClientesController.class).getCliente(id)).withSelfRel());
        return ResponseEntity.ok(cliente);
    }

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<PagedModel<Cliente>> getClientes(@RequestParam(defaultValue = "", required = false) String contains,
                                                           @RequestParam(defaultValue = "0", required = false) int page,
                                                           @RequestParam(defaultValue = "2", required = false) int size) {
        Page<Cliente> clientes = clientesService.buscarClientes(contains, page, size);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(clientes, clientesModelAssembler));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> actualizarCliente(@PathVariable Integer id, @Valid @RequestBody Cliente clienteActualizado) {
        clientesService.actualizarCliente(id, clienteActualizado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Integer id) {
        clientesService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}/recomendaciones")
    public ResponseEntity<ClienteRecomendaciones> getRecomendaciones(@PathVariable Integer id) {
        return ResponseEntity.ok(clientesService.obtenerRecomendaciones(id).add(linkTo(methodOn(ClientesController.class).getRecomendaciones(id)).withSelfRel()));
    }

    @GetMapping(value = "/{id}/estadisticas")
    public ResponseEntity<ClienteEstadisticas> getEstadisticas(@PathVariable Integer id,
                                                               @RequestParam(required = false) String bodega,
                                                               @RequestParam(required = false) String origen,
                                                               @RequestParam(required = false) String tipo,
                                                               @RequestParam(required = false) Integer anyo,
                                                               @RequestParam(required = false) String uva,
                                                               @RequestParam(required = false) LocalDate desde,
                                                               @RequestParam(required = false) LocalDate hasta) {

        return ResponseEntity.ok(clientesService.obtenerEstadisticas(id, bodega, origen, tipo, anyo, uva, desde, hasta).add(linkTo(methodOn(ClientesController.class)
                .getEstadisticas(id, bodega, origen, tipo, anyo, uva, desde, hasta)).withSelfRel()));
    }

    @PostMapping(value = "/{id}/vinos/{id_vino}")
    public ResponseEntity<Void> addVinoCliente(@PathVariable Integer idCliente, @PathVariable Integer idVino, @Valid @RequestBody UsuarioVino relacion) {
        Cliente cliente = clientesService.buscarPorId(idCliente)
                .orElseThrow(() -> new ClienteNotFoundException(idCliente));
        Vino vino = vinoService.buscarPorId(idVino)
                .orElseThrow(() -> new VinoNotFoundException(idVino));

        UsuarioVinoId usuarioVinoId = new UsuarioVinoId(idCliente, idVino);

        usuarioVinoService.aniadeVinoUsuario(
                usuarioVinoId,
                cliente,
                vino,
                relacion.getPuntuacion(),
                relacion.getFechaAdicion()
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}/vinos")
    public ResponseEntity<PagedModel<UsuarioVino>> getVinosDeCliente(@PathVariable Integer id,
                                                                     @RequestParam(required = false) String bodega,
                                                                     @RequestParam(required = false) String origen,
                                                                     @RequestParam(required = false) String tipo,
                                                                     @RequestParam(required = false) Integer anyo,
                                                                     @RequestParam(required = false) String uva,
                                                                     @RequestParam(required = false) LocalDate desde,
                                                                     @RequestParam(required = false) LocalDate hasta,
                                                                     @RequestParam(defaultValue = "0", required = false) int page,
                                                                     @RequestParam(defaultValue = "2", required = false) int size) {

        Page<UsuarioVino> vinos = usuarioVinoService.buscarVinosDeCliente(
                id, bodega, origen, tipo, anyo, uva, desde, hasta, page, size);
        return ResponseEntity.ok(usuarioVinoPagedResourcesAssembler.toModel(vinos, usuarioVinoModelAssembler));
    }

    @GetMapping(value = "/{id}/vinos/{id_vino}")
    public ResponseEntity<UsuarioVino> getVinoDeCliente(@PathVariable Integer id, @PathVariable Integer id_vino) {

        UsuarioVino relacion = usuarioVinoService.buscarRelacionPorId(id_vino,id);
        return ResponseEntity.ok(relacion.add(linkTo(methodOn(ClientesController.class).getVinoDeCliente(id,id_vino)).withSelfRel()));
    }

    @PutMapping(value = "/{id}/vinos/{id_vino}")
    public ResponseEntity<Void> updatePuntuacion(@PathVariable Integer id,@PathVariable Integer id_vino, @Valid @RequestBody UsuarioVino usuarioVino) {
        usuarioVinoService.modificaPuntuacion(id_vino,id,usuarioVino.getPuntuacion());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}/vinos/{id_vino}")
    public ResponseEntity<Void> deleteVinoFromUsuario(@PathVariable Integer id,@PathVariable Integer id_vino){
        usuarioVinoService.eliminaVinoDeLista(id_vino,id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/seguimientos/{id_seguido}")
    public ResponseEntity<Void> crearSeguimiento(@PathVariable Integer id,@PathVariable Integer id_seguido){
        Seguimiento seguimiento = seguimientoService.crearSolicitud(id,id_seguido);
        return ResponseEntity.created(linkTo(methodOn(ClientesController.class).getSeguimiento(id,id_seguido)).toUri()).build();
    }

    @GetMapping(value = "/{id}/seguimientos")
    public ResponseEntity<PagedModel<Seguimiento>> getSeguimientos(
            @PathVariable Integer id,
            @RequestParam(required = false) EstadoSeguimiento estado,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "2", required = false) int size) {

        Page<Seguimiento> seguimientos = seguimientoService.buscarSeguimientos(id, estado, page, size);
        return ResponseEntity.ok(seguimientoPagedAssembler.toModel(seguimientos, seguimientoModelAssembler));
    }

    @GetMapping(value = "/{id}/seguimientos/{id_seguido}")
    public ResponseEntity<Seguimiento> getSeguimiento(
            @PathVariable Integer id,
            @PathVariable Integer id_seguido) {

        Seguimiento seg = seguimientoService.buscarEntreDosClientes(id, id_seguido);
        seg.add(linkTo(methodOn(ClientesController.class)
                .getSeguimiento(id, id_seguido)).withSelfRel());
        return ResponseEntity.ok(seg);
    }

    @PutMapping("/{id}/seguimientos/{id_seguido}")
    public ResponseEntity<Void> actualizarEstadoSeguimient(
            @PathVariable Integer id,
            @PathVariable Integer id_seguido,
            @RequestParam EstadoSeguimiento estado){
        seguimientoService.actualizarEstado(id,id_seguido,estado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}/seguimientos/{id_seguido}")
    public ResponseEntity<Void> deleteSeguimiento(@PathVariable Integer id,@PathVariable Integer id_seguido){
        seguimientoService.eliminarSeguimiento(id,id_seguido);
        return ResponseEntity.noContent().build();
    }
}
