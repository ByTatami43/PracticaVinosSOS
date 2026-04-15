package es.upm.sos.vinos.service;

import es.upm.sos.vinos.exception.ClienteNotFoundException;
import es.upm.sos.vinos.exception.SeguimientoNotFoundException;
import es.upm.sos.vinos.model.*;
import es.upm.sos.vinos.repository.ClientesRepository;
import es.upm.sos.vinos.repository.SeguimientoRepository;
import es.upm.sos.vinos.repository.UsuarioVinoRepository;
import es.upm.sos.vinos.repository.VinoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
public class SeguimientoService {

    private VinoRepository vinoRepository;
    private ClientesRepository clientesRepository;
    private UsuarioVinoRepository usuarioVinoRepository;
    private SeguimientoRepository seguimientoRepository;



    public Seguimiento crearSolicitud(Integer solicitanteId, Integer seguidoId) {
        Cliente solicitante = clientesRepository.findById(solicitanteId)
                .orElseThrow(() -> new ClienteNotFoundException(solicitanteId));
        Cliente seguido = clientesRepository.findById(seguidoId)
                .orElseThrow(() -> new ClienteNotFoundException(seguidoId));

        Seguimiento seguimiento = new Seguimiento();
        seguimiento.setSolicitante(solicitante);
        seguimiento.setSeguido(seguido);
        seguimiento.setEstado(EstadoSeguimiento.PENDIENTE);
        return seguimientoRepository.save(seguimiento);
    }

    // Buscar seguimiento por id
    public Seguimiento buscarPorId(Integer id) {
        return seguimientoRepository.findById(id)
                .orElseThrow(() -> new SeguimientoNotFoundException(id));
    }

    // Buscar seguimiento concreto entre dos clientes
    public Seguimiento buscarEntreDosClientes(Integer solicitanteId, Integer seguidoId) {
        return seguimientoRepository.findBySolicitante_IdAndSeguido_Id(solicitanteId, seguidoId)
                .orElseThrow(() -> new SeguimientoNotFoundException(solicitanteId, seguidoId));
    }
    // Solicitudes enviadas por un cliente (él es el solicitante)
    public Page<Seguimiento> buscarSolicitudesEnviadas(Integer clienteId, int page, int size) {
        if (!clientesRepository.existsById(clienteId))
            throw new ClienteNotFoundException(clienteId);
        return seguimientoRepository.findBySolicitante_Id(clienteId, PageRequest.of(page, size));
    }

    // Solicitudes recibidas por un cliente (él es el seguido) → "pendientes"
    public Page<Seguimiento> buscarSolicitudesRecibidas(Integer clienteId, int page, int size) {
        if (!clientesRepository.existsById(clienteId))
            throw new ClienteNotFoundException(clienteId);
        return seguimientoRepository.findBySeguido_Id(clienteId, PageRequest.of(page, size));
    }

    // Solicitudes pendientes recibidas por un cliente
    public Page<Seguimiento> buscarPendientesRecibidos(Integer clienteId, int page, int size) {
        if (!clientesRepository.existsById(clienteId))
            throw new ClienteNotFoundException(clienteId);
        return seguimientoRepository.findBySeguido_IdAndEstado(
                clienteId, EstadoSeguimiento.PENDIENTE, PageRequest.of(page, size));
    }

    // Aceptar o rechazar una solicitud
    public Seguimiento actualizarEstado(Integer solicitanteId, Integer seguidoId, EstadoSeguimiento nuevoEstado) {
        Seguimiento seguimiento = buscarEntreDosClientes(solicitanteId, seguidoId);
        seguimiento.setEstado(nuevoEstado);
        return seguimientoRepository.save(seguimiento);
    }

    // Dejar de seguir (eliminar el seguimiento)
    public void eliminarSeguimiento(Integer solicitanteId, Integer seguidoId) {
        Seguimiento seguimiento = buscarEntreDosClientes(solicitanteId, seguidoId);
        seguimientoRepository.delete(seguimiento);
    }

}
