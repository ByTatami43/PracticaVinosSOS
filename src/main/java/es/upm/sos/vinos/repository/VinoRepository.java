package es.upm.sos.vinos.repository;


import es.upm.sos.vinos.model.Vino;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VinoRepository extends JpaRepository<Vino,Integer> {
    boolean existsByNombre(String nombre);
    Page<Vino> findByNombreStartsWith(@Param("starts_with") String nombreEmpiezaCon, Pageable paginable);
    @Query("SELECT v FROM Vino v WHERE " +
            "(:tipo IS NULL OR v.tipoVino = :tipo) AND " +
            "(:nombre IS NULL OR v.nombre = :nombre) AND " +
            "(:uva IS NULL OR v.uva = :uva) AND " +
            "(:origen IS NULL OR v.origen = :origen) AND " +
            "(:bodega IS NULL OR v.bodega = :bodega)")
    Page<Vino> findByFiltros(@Param("tipo") String tipo,
                             @Param("nombre") String nombre,
                             @Param("uva") String uva,
                             @Param("origen") String origen,
                             @Param("bodega") String bodega,
                             Pageable paginable);
}
