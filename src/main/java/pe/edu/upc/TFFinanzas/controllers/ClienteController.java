package pe.edu.upc.TFFinanzas.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import lombok.RequiredArgsConstructor;
import pe.edu.upc.TFFinanzas.dtos.ClienteDTO;
import pe.edu.upc.TFFinanzas.dtos.auth.ResponseDTO;
import pe.edu.upc.TFFinanzas.services.ClienteService;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;

    //registrar cliente
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCliente(@RequestBody ClienteDTO clienteDTO){
        try{
            return new ResponseEntity<>(clienteService.registrarCliente(clienteDTO), HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(new ResponseDTO("Error en el registro"), HttpStatus.BAD_REQUEST);
        }
    }

    //actualizar cliente
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO){
        try {
            return new ResponseEntity<>(clienteService.actualizarCliente(id, clienteDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO("Error al actualizar el cliente"), HttpStatus.BAD_REQUEST);
        }
    }

    //LISTAR TODOS LOS CLIENTES 
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/listar")
    public ResponseEntity<List<ClienteDTO>> listarTodosLosClientes() {
        List<ClienteDTO> clientes = clienteService.listarTodosLosClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }
    //BUSCAR CLIENTE POR NOMBRE
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<ClienteDTO>> buscarClientePorNombre(@PathVariable String nombre){
        List<ClienteDTO> clientes = clienteService.buscarClientePorNombre(nombre);
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }
    //eliminar cliente 
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(clienteService.eliminarCliente(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO("Error al eliminar el cliente"), HttpStatus.BAD_REQUEST);
        }
    }
}
