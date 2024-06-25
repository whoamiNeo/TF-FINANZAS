package pe.edu.upc.TFFinanzas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.TFFinanzas.entities.DetalleCredito;

import java.util.List;

@Repository
public interface DetalleCreditoRepository extends JpaRepository<DetalleCredito, Long> {
    List<DetalleCredito> findByCreditoIdCredito(Long idCredito);
}
