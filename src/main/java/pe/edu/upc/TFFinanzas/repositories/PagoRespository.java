package pe.edu.upc.TFFinanzas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.edu.upc.TFFinanzas.entities.Pago;

@Repository
public interface PagoRespository extends JpaRepository<Pago, Long>{
    List<Pago> findByIdPago(Long idPago);
}
