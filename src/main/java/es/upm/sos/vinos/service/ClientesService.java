package es.upm.sos.vinos.service;


import es.upm.sos.vinos.exception.ClienteNotFoundException;
import es.upm.sos.vinos.exception.UsuarioMenorDeEdadException;
import es.upm.sos.vinos.model.Cliente;
import es.upm.sos.vinos.model.ClienteEstadisticas;
import es.upm.sos.vinos.model.ClienteRecomendaciones;
import es.upm.sos.vinos.model.UsuarioVino;
import es.upm.sos.vinos.repository.ClientesRepository;
import es.upm.sos.vinos.repository.UsuarioVinoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientesService {
    private final ClientesRepository repository;
    private final UsuarioVinoRepository usuarioVinoRepository;

    public boolean existeCliente(String nombre){
        return repository.existsByNombre(nombre);
    }

    public Cliente crearCliente(Cliente cliente){
        LocalDate hoy = LocalDate.now();
        if(ChronoUnit.YEARS.between(hoy,cliente.getFechaNacimiento())<18){
            throw new UsuarioMenorDeEdadException();
        }
        return repository.save(cliente);
    }

    public Optional<Cliente> buscarPorId(Integer id){
        return repository.findById(id);
    }

    public boolean existeClientePorId(Integer id){
        return repository.existsById(id);
    }

    public Page<Cliente> buscarClientes(String contains, int page, int size){
        Pageable paginable = PageRequest.of(page,size);
        if(contains!=null && !contains.isEmpty()){
            return repository.findByNombreContains(contains,paginable);
        }else{
            return repository.findAll(paginable);
        }
    }

    public Cliente actualizarCliente(Integer id, Cliente clienteActualizado) {
        return repository.findById(id)
                .map(cliente -> {
                    cliente.setNombre(clienteActualizado.getNombre());
                    cliente.setCorreo(clienteActualizado.getCorreo());
                    cliente.setFechaNacimiento(clienteActualizado.getFechaNacimiento());
                    return repository.save(cliente);
                })
                .orElseThrow(() -> new ClienteNotFoundException(id));
    }

    public void eliminarCliente(Integer id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }else{
            throw new ClienteNotFoundException(id);
        }
    }

    public ClienteRecomendaciones obtenerRecomendaciones(Integer clienteId){
        Cliente cliente = repository.findById(clienteId).orElseThrow(()->new ClienteNotFoundException(clienteId));
        Pageable top5 = PageRequest.of(0,5);

        List<UsuarioVino> ultimos = usuarioVinoRepository
                .findByCliente_IdOrderByFechaAdicionDesc(clienteId, top5).getContent();

        List<UsuarioVino> mejores = usuarioVinoRepository
                .findByCliente_IdOrderByPuntuacionDesc(clienteId, top5).getContent();

        List<UsuarioVino> amigos = usuarioVinoRepository
                .findMejoresVinosDeAmigos(clienteId, top5).getContent();
        return new ClienteRecomendaciones(cliente, ultimos, mejores, amigos);
    }

    public ClienteEstadisticas obtenerEstadisticas(
            Integer clienteId, String bodega, String origen,
            String tipo, Integer anyo, String uva,
            LocalDate desde, LocalDate hasta) {

        if (!repository.existsById(clienteId))
            throw new ClienteNotFoundException(clienteId);

        Double media = usuarioVinoRepository.calcularMedia(
                clienteId, bodega, origen, tipo, anyo, uva, desde, hasta);

        return new ClienteEstadisticas(clienteId, media != null ? media : 0.0, 0L);
    }

}
