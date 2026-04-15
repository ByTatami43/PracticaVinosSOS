package es.upm.sos.vinos.repository;

import es.upm.sos.vinos.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ClientesRepository extends JpaRepository<Cliente,Integer> {


    boolean existsByNombre(String nombre);
    Page<Cliente> findByNombreStartsWith(@Param("starts_with") String nombreEmpiezaCon, Pageable paginable);
    Page<Cliente> findByNombreContains(@Param("contains") String nombreContiene, Pageable paginable);


}
