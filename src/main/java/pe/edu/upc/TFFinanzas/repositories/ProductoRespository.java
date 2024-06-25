package pe.edu.upc.TFFinanzas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.edu.upc.TFFinanzas.entities.Producto;

import java.util.List;

@Repository
public interface ProductoRespository extends JpaRepository<Producto, Long>{

    //BUSCAR PRODUCTO POR NOMBRE
    List<Producto> findByNombreProducto(String nombreProducto);
    
}
