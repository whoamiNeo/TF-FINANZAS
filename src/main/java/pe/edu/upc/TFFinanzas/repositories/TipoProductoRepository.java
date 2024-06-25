package pe.edu.upc.TFFinanzas.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.edu.upc.TFFinanzas.entities.TipoProducto;


@Repository
public interface TipoProductoRepository extends JpaRepository<TipoProducto, Long>{

    //? buscar tipo producto por descripcion 
    Optional<TipoProducto> findByDescripcion(String descripcion);
    

}
