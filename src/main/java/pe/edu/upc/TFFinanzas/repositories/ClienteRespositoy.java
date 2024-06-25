package pe.edu.upc.TFFinanzas.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.edu.upc.TFFinanzas.entities.Cliente;

@Repository
public interface ClienteRespositoy extends JpaRepository<Cliente, Long>{
    
    ///ACTUALIZAR CLIENTES 
    ///LISTAR TOODS LOS CLIENTES REGISTRADOS-> FindAll ya esta por defecto 
    ///BUSCAR POR NOMBRE DEL CLIENTE 
    List<Cliente> findByNombre(String nombre);

    ///BUSCAR POR NUMERO DE DOCUMENTO AL CLIENTE 
    Optional<Cliente> findByNumeroDocumento(int numeroDocumento);
    
}
