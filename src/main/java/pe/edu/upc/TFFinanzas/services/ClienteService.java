package pe.edu.upc.TFFinanzas.services;

import lombok.RequiredArgsConstructor;
import pe.edu.upc.TFFinanzas.dtos.ClienteDTO;
import pe.edu.upc.TFFinanzas.dtos.auth.ResponseDTO;
import pe.edu.upc.TFFinanzas.entities.Cliente;
import pe.edu.upc.TFFinanzas.repositories.ClienteRespositoy;
import pe.edu.upc.TFFinanzas.repositories.UserRepository;


import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRespositoy clienteRespositoy;
    private final UserRepository userRepository;

    /// REGISTRAR CLIENTE
    public ResponseDTO registrarCliente(ClienteDTO clienteDTO) {
        if(!userRepository.findById(clienteDTO.getIdUsuario()).isPresent()){
            return new ResponseDTO("Usuario no encontrado");
        }
        if (clienteRespositoy.findByNumeroDocumento(clienteDTO.getNumeroDocumento()).isPresent()) {
            return new ResponseDTO("El cliente ya esta registrado");
        }
        
        Cliente cliente= Cliente.builder()
                .nombre(clienteDTO.getNombre())
                .apellido(clienteDTO.getApellido())
                .correo(clienteDTO.getCorreo())
                .numeroDocumento(clienteDTO.getNumeroDocumento())
                .telefono(clienteDTO.getTelefono())
                .limiteCredito(clienteDTO.getLimiteCredito())
                .direccion(clienteDTO.getDireccion())
                .users(userRepository.findById(clienteDTO.getIdUsuario()).get())
                .build();
        clienteRespositoy.save(cliente);
        return new ResponseDTO("Cliente registrado correctamente");
    }

    /// ACTUALIZAR CLIENTE
    public ResponseDTO actualizarCliente(Long idCliente, ClienteDTO clienteDTO){
        //verificar si existe el cliente
        Optional<Cliente> clienteExistente = clienteRespositoy.findById(idCliente);
        if(!clienteExistente.isPresent()){
            return new ResponseDTO("Cliente no encontrado");
        }
        
        //verificar si existe el usuario
        if(!userRepository.findById(clienteDTO.getIdUsuario()).isPresent()){
            return new ResponseDTO("Usuario no encontrado");
        }
        
        //verificar si existe un cliente con el mismo numero de documento
        if (clienteRespositoy.findByNumeroDocumento(clienteDTO.getNumeroDocumento()).isPresent()) {
            return new ResponseDTO("El cliente no se puede actualizar, ya existe un cliente con el mismo numero de documento");
        }

        //obtener cliente y usuario
        Cliente cliente=Cliente.builder()
                .nombre(clienteDTO.getNombre())
                .apellido(clienteDTO.getApellido())
                .correo(clienteDTO.getCorreo())
                .numeroDocumento(clienteDTO.getNumeroDocumento())
                .telefono(clienteDTO.getTelefono())
                .limiteCredito(clienteDTO.getLimiteCredito())
                .direccion(clienteDTO.getDireccion())
                .users(userRepository.findById(clienteDTO.getIdUsuario()).get())
                .build();
        clienteRespositoy.save(cliente);
        return new ResponseDTO("Cliente actualizado correctamente");
        
    }

    /// LISTAR TODOS LOS CLIENTES
    public List<ClienteDTO> listarTodosLosClientes() {
        List<Cliente> clientes = clienteRespositoy.findAll();
        return clientes.stream()
                .map(cliente -> new ClienteDTO(
                        cliente.getNombre(),
                        cliente.getApellido(),
                        cliente.getCorreo(),
                        cliente.getNumeroDocumento(),
                        cliente.getTelefono(),
                        cliente.getLimiteCredito(),
                        cliente.getDireccion(),
                        cliente.getUsers().getId()))
                        
                .collect(Collectors.toList());
    }

    /// BUSCAR CLIENTE POR NOMBRE
    public List<ClienteDTO> buscarClientePorNombre(String nombre) {
        List<Cliente> clientes = clienteRespositoy.findByNombre(nombre);
        return clientes.stream()
                .map(cliente -> new ClienteDTO(
                        cliente.getNombre(),
                        cliente.getApellido(),
                        cliente.getCorreo(),
                        cliente.getNumeroDocumento(),
                        cliente.getTelefono(),
                        cliente.getLimiteCredito(),
                        cliente.getDireccion(),
                        cliente.getUsers().getId()))
                .collect(Collectors.toList());
    }
    
    ///ELIMINAR CLIENTE
    public ResponseDTO eliminarCliente(Long idCliente) {
        if(clienteRespositoy.existsById(idCliente)){
            clienteRespositoy.deleteById(idCliente);
            return new ResponseDTO("Cliente eliminado correctamente");
        }
        return new ResponseDTO("Cliente no encontrado");
    }
}