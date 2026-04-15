package es.upm.sos.vinos.service;


import es.upm.sos.vinos.exception.ClienteNotFoundException;
import es.upm.sos.vinos.exception.SeguimientoNotFoundException;
import es.upm.sos.vinos.exception.UsuarioVinoNotFoundException;
import es.upm.sos.vinos.exception.VinoNotFoundException;
import es.upm.sos.vinos.model.*;
import es.upm.sos.vinos.repository.ClientesRepository;
import es.upm.sos.vinos.repository.SeguimientoRepository;
import es.upm.sos.vinos.repository.UsuarioVinoRepository;
import es.upm.sos.vinos.repository.VinoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class UsuarioVinoService {
    private UsuarioVinoRepository usuarioVinoRepository;
    private final ClientesRepository clientesRepository;
    private final VinoRepository vinoRepository;
    private final SeguimientoRepository seguimientoRepository;


    public UsuarioVino aniadeVinoUsuario(UsuarioVinoId usuarioVinoId, Cliente cliente, Vino vino, Integer puntuacion, LocalDate fecha){
        usuarioVinoId.setClienteId(cliente.getId());

        UsuarioVino relacion = new UsuarioVino();
        relacion.setVino(vino);
        relacion.setCliente(cliente);
        relacion.setPuntuacion(puntuacion);
        relacion.setFechaAdicion(fecha);
        return usuarioVinoRepository.save(relacion);
    }

    public Page<UsuarioVino> buscarVinoDeCliente(Integer clienteId, int page, int size){
        if(!clientesRepository.existsById(clienteId)){
            throw new ClienteNotFoundException(clienteId);
        }
        return usuarioVinoRepository.findByCliente_Id(clienteId, PageRequest.of(page,size));
    }

    public UsuarioVino modificaPuntuacion(Integer vinoId, Integer clienteId, Integer puntuacion){
        UsuarioVinoId usuarioVinoId = new UsuarioVinoId(clienteId,vinoId);
        UsuarioVino relacion = buscarRelacionPorId(usuarioVinoId);
        relacion.setPuntuacion(puntuacion);
        return usuarioVinoRepository.save(relacion);
    }

    public void eliminaVinoDeLista(Integer vinoId, Integer clienteId, Integer puntuacion){
        UsuarioVinoId usuarioVinoId = new UsuarioVinoId(clienteId,vinoId);

        if(!usuarioVinoRepository.existsById(usuarioVinoId)){
            throw new UsuarioVinoNotFoundException(clienteId,vinoId);
        }

        usuarioVinoRepository.deleteById(usuarioVinoId);
    }

    public UsuarioVino buscarRelacionPorId(UsuarioVinoId usuarioVinoId){
        return usuarioVinoRepository.findById(usuarioVinoId).orElseThrow(()->new UsuarioVinoNotFoundException(usuarioVinoId.getClienteId(), usuarioVinoId.getVinoId()));
    }
    public UsuarioVino buscarRelacionPorId(Integer vinoId, Integer clienteId){
        return buscarRelacionPorId(new UsuarioVinoId(clienteId, vinoId));
    }

    public Page<UsuarioVino> buscarVinosDeSeguidoConFiltros(
            Integer solicitanteId, Integer seguidoId,
            String bodega, String origen, String tipo, Integer anyo,
            LocalDate desde, LocalDate hasta, int page, int size) {

        // Validar que realmente sigue a ese usuario y está ACEPTADA
        Seguimiento seg = seguimientoRepository
                .findBySolicitante_IdAndSeguido_Id(solicitanteId, seguidoId)
                .orElseThrow(() -> new SeguimientoNotFoundException(solicitanteId, seguidoId));

        if (seg.getEstado() != EstadoSeguimiento.ACEPTADA) {
            throw new SeguimientoNotFoundException(solicitanteId, seguidoId);
        }

        return usuarioVinoRepository.findByClienteIdConFiltros(
                seguidoId, bodega, origen, tipo, anyo, desde, hasta,
                PageRequest.of(page, size)
        );
    }

}
