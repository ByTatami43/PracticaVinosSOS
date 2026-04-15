package es.upm.sos.vinos.repository;

import es.upm.sos.vinos.model.EstadoSeguimiento;
import es.upm.sos.vinos.model.Seguimiento;
import es.upm.sos.vinos.model.UsuarioVino;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SeguimientoRepository extends JpaRepository<Seguimiento,Integer> {

    Page<Seguimiento> findByEstado(EstadoSeguimiento estado, Pageable pageable);
    Page<Seguimiento> findBySolicitante_Id(Integer clienteId, Pageable pageable);
    Page<Seguimiento> findBySeguido_Id(Integer clienteId, Pageable pageable);
    Page<Seguimiento> findBySeguido_IdAndEstado(Integer seguidoId,EstadoSeguimiento estado, Pageable pageable);
    Optional<Seguimiento> findBySolicitante_IdAndSeguido_Id(Integer solicitanteId, Integer seguidoId);


}
