package es.upm.sos.vinos.controller;

import es.upm.sos.vinos.assembler.VinoModelAssembler;
import es.upm.sos.vinos.exception.VinoExistsException;
import es.upm.sos.vinos.exception.VinoNotFoundException;
import es.upm.sos.vinos.model.Vino;
import es.upm.sos.vinos.service.VinoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@AllArgsConstructor
@RequestMapping("/vinos")

public class VinosController {
    private final VinoService vinoService;
    private final VinoModelAssembler vinoModelAssembler;
    private PagedResourcesAssembler<Vino> pagedResourcesAssembler;

    @PostMapping
    public ResponseEntity<Void> crearVino(@Valid @RequestBody Vino vino){
        if (!vinoService.existeVinoPorNombre(vino.getNombre())) {
            Vino aux = vinoService.crearVino(vino);
            return ResponseEntity.created(linkTo(VinosController.class).slash(aux.getId()).toUri()).build();
        }
        throw new VinoExistsException(vino.getNombre());
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<Vino> getVino(@PathVariable Integer id){
        Vino vino = vinoService.buscarPorId(id).orElseThrow(()->new VinoNotFoundException(id));
        vino.add(linkTo(methodOn(VinosController.class).getVino(id)).withSelfRel());
        return ResponseEntity.ok(vino);
    }

    @GetMapping
    public ResponseEntity<PagedModel<Vino>> getVinos(@RequestParam(defaultValue = "0", required = false) int page,
                                                     @RequestParam(defaultValue = "2", required = false) int size,
                                                    @RequestParam(required = false) String bodega,
                                                    @RequestParam(required = false) String origen,
                                                    @RequestParam(required = false) String tipo,
                                                    @RequestParam(required = false) String uva){
        Page<Vino> vinos = vinoService.buscarVinos(tipo,uva,origen,bodega,page,size);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(vinos,vinoModelAssembler));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> actualizarVino(@PathVariable Integer id,
                                               @Valid @RequestBody Vino vinoActualizado){
        vinoService.actualizarVino(id,vinoActualizado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> eliminarVino(@PathVariable Integer id){
        vinoService.eliminarVino(id);
        return ResponseEntity.noContent().build();
    }
}
