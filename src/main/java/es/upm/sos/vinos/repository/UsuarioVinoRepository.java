package es.upm.sos.vinos.repository;

import es.upm.sos.vinos.model.Cliente;
import es.upm.sos.vinos.model.UsuarioVino;
import es.upm.sos.vinos.model.UsuarioVinoId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface UsuarioVinoRepository extends JpaRepository<UsuarioVino, UsuarioVinoId> {


    Page<UsuarioVino> findByCliente_Id(Integer clienteId, Pageable pageable);

    Page<UsuarioVino> findByVino_Id(Integer vinoId, Pageable pageable);

    // En UsuarioVinoRepository
    @Query("SELECT uv FROM UsuarioVino uv WHERE uv.cliente.id = :seguidoId " +
            "AND (:bodega IS NULL OR uv.vino.bodega = :bodega) " +
            "AND (:origen IS NULL OR uv.vino.origen = :origen) " +
            "AND (:tipo IS NULL OR uv.vino.tipoVino = :tipo) " +
            "AND (:anyo IS NULL OR uv.vino.añada = :anyo) " +
            "AND (:desde IS NULL OR uv.fechaAdicion >= :desde) " +
            "AND (:hasta IS NULL OR uv.fechaAdicion <= :hasta)")
    Page<UsuarioVino> findByClienteIdConFiltros(
            @Param("seguidoId") Integer seguidoId,
            @Param("bodega") String bodega,
            @Param("origen") String origen,
            @Param("tipo") String tipo,
            @Param("anyo") Integer anyo,
            @Param("uva") String uva,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta,
            Pageable pageable
    );

    // 5 últimos añadidos por fecha
    Page<UsuarioVino> findByCliente_IdOrderByFechaAdicionDesc(Integer clienteId, Pageable pageable);

    // 5 con mayor puntuación propia
    Page<UsuarioVino> findByCliente_IdOrderByPuntuacionDesc(Integer clienteId, Pageable pageable);

    // 5 con mayor puntuación de todos sus amigos (usuarios ACEPTADOS)
    @Query("SELECT uv FROM UsuarioVino uv WHERE uv.cliente.id IN " +
            "(SELECT s.seguido.id FROM Seguimiento s WHERE s.solicitante.id = :clienteId " +
            "AND s.estado = 'ACEPTADA') " +
            "ORDER BY uv.puntuacion DESC")
    Page<UsuarioVino> findMejoresVinosDeAmigos(@Param("clienteId") Integer clienteId, Pageable pageable);

    @Query("SELECT AVG(uv.puntuacion) FROM UsuarioVino uv WHERE uv.cliente.id = :clienteId " +
            "AND (:bodega IS NULL OR uv.vino.bodega = :bodega) " +
            "AND (:origen IS NULL OR uv.vino.origen = :origen) " +
            "AND (:tipo IS NULL OR uv.vino.tipoVino = :tipo) " +
            "AND (:anyo IS NULL OR uv.vino.añada = :anyo) " +
            "AND (:uva IS NULL OR uv.vino.uva = :uva) " +
            "AND (:desde IS NULL OR uv.fechaAdicion >= :desde) " +
            "AND (:hasta IS NULL OR uv.fechaAdicion <= :hasta)")
    Double calcularMedia(
            @Param("clienteId") Integer clienteId,
            @Param("bodega") String bodega,
            @Param("origen") String origen,
            @Param("tipo") String tipo,
            @Param("anyo") Integer anyo,
            @Param("uva") String uva,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta
    );
}
