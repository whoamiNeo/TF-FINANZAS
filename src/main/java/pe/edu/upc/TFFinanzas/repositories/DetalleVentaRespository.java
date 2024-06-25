package pe.edu.upc.TFFinanzas.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.edu.upc.TFFinanzas.entities.DetalleVenta;

@Repository
public interface DetalleVentaRespository  extends JpaRepository<DetalleVenta, Long>{
    ///CALCULAR EL MONTO TOTAL DE LA VENTA POR CLIENTE
    @Query("SELECT SUM(dv.precio * dv.cantidad) FROM DetalleVenta dv WHERE dv.cliente.idCliente = :idCliente")
    Float calcularTotalComprasPorCliente(Long idCliente);
}
