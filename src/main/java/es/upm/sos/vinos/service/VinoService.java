package es.upm.sos.vinos.service;

import es.upm.sos.vinos.model.Vino;
import es.upm.sos.vinos.repository.VinoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VinoService {

    private final VinoRepository repository;

    public boolean existeVino(String nombre){
        return repository.existsByNombre(nombre);
    }

    public Vino crearVino(Vino vino){
        return repository.save(vino);
    }

    public Optional<Vino> buscarPorId(Integer id){
        return repository.findById(id);
    }

    public Page<Vino> buscarVinos(String tipo, String uva, String origen, String bodega, int page, int size){
        Pageable paginable = PageRequest.of(page,size);
        return repository.findByFiltros(tipo,uva,origen,bodega,paginable);
    }

    public Vino actualizarVino(Integer id, Vino vino){
        vino.setId(id);
        return repository.save(vino);
    }

    public void eliminarVino(Integer id){
        repository.deleteById(id);
    }

    public boolean existeVinoPorId(Integer id){
        return repository.existsById(id);
    }

    public boolean existeVinoPorNombre(String nombre){
        return repository.existsByNombre(nombre);
    }
}
