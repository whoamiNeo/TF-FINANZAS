package pe.edu.upc.TFFinanzas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upc.TFFinanzas.dtos.DetalleVentaDTO;
import pe.edu.upc.TFFinanzas.dtos.MontoTotalDetalleVentaDTO;
import pe.edu.upc.TFFinanzas.entities.DetalleVenta;
import pe.edu.upc.TFFinanzas.entities.Producto;
import pe.edu.upc.TFFinanzas.entities.Cliente;
import pe.edu.upc.TFFinanzas.repositories.DetalleVentaRespository;
import pe.edu.upc.TFFinanzas.repositories.ProductoRespository;
import pe.edu.upc.TFFinanzas.repositories.ClienteRespositoy;
import pe.edu.upc.TFFinanzas.dtos.auth.ResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DetalleVentaService {
    private final DetalleVentaRespository detalleVentaRepository;
    private final ProductoRespository productoRepository;
    private final ClienteRespositoy clienteRepository;

    // REGISTRAR DETALLE VENTA
    public ResponseDTO registrarDetalleVenta(DetalleVentaDTO detalleVentaDTO) {
        Optional<Producto> productoOpt = productoRepository.findById(detalleVentaDTO.getIdProducto());
        if (!productoOpt.isPresent()) {
            return new ResponseDTO("Producto no encontrado");
        }

        Optional<Cliente> clienteOpt = clienteRepository.findById(detalleVentaDTO.getIdCliente());
        if (!clienteOpt.isPresent()) {
            return new ResponseDTO("Cliente no encontrado");
        }

        Producto producto = productoOpt.get();
        Cliente cliente = clienteOpt.get();
        
        DetalleVenta detalleVenta = new DetalleVenta();
        detalleVenta.setCantidad(detalleVentaDTO.getCantidad());
        detalleVenta.setPrecio(detalleVentaDTO.getPrecio());
        detalleVenta.setFechaVenta(detalleVentaDTO.getFechaVenta());
        detalleVenta.setProducto(producto);
        detalleVenta.setCliente(cliente);

        detalleVentaRepository.save(detalleVenta);
        return new ResponseDTO("Detalle de venta registrado correctamente");
    }

    // ACTUALIZAR DETALLE VENTA
    public ResponseDTO actualizarDetalleVenta(Long idDetalleVenta, DetalleVentaDTO detalleVentaDTO) {
        Optional<DetalleVenta> detalleVentaOpt = detalleVentaRepository.findById(idDetalleVenta);
        if (!detalleVentaOpt.isPresent()) {
            return new ResponseDTO("Detalle de venta no encontrado");
        }

        Optional<Producto> productoOpt = productoRepository.findById(detalleVentaDTO.getIdProducto());
        if (!productoOpt.isPresent()) {
            return new ResponseDTO("Producto no encontrado");
        }

        Optional<Cliente> clienteOpt = clienteRepository.findById(detalleVentaDTO.getIdCliente());
        if (!clienteOpt.isPresent()) {
            return new ResponseDTO("Cliente no encontrado");
        }

        DetalleVenta detalleVenta = detalleVentaOpt.get();
        Producto producto = productoOpt.get();
        Cliente cliente = clienteOpt.get();
        
        detalleVenta.setCantidad(detalleVentaDTO.getCantidad());
        detalleVenta.setPrecio(detalleVentaDTO.getPrecio());
        detalleVenta.setFechaVenta(detalleVentaDTO.getFechaVenta());
        detalleVenta.setProducto(producto);
        detalleVenta.setCliente(cliente);

        detalleVentaRepository.save(detalleVenta);
        return new ResponseDTO("Detalle de venta actualizado correctamente");
    }

     //LISTAR DETALLE VENTA DE UN CLIENTES ASOCIADOS AL USUARIO QUE LE REGISTRÓ
    // LISTAR DETALLES DE VENTA POR CLIENTE
    // LISTAR DETALLES DE VENTA POR CLIENTE
    public MontoTotalDetalleVentaDTO listarDetallesVentaPorCliente(Long idCliente) {
        List<DetalleVenta> detallesVenta = detalleVentaRepository.findAll()
                .stream()
                .filter(detalleVenta -> detalleVenta.getCliente().getIdCliente().equals(idCliente))
                .collect(Collectors.toList());

        double montoTotal = calcularMontoTotal(idCliente);

        List<DetalleVentaDTO> detallesVentaDTO = detallesVenta.stream()
                .map(detalleVenta -> new DetalleVentaDTO(
                        detalleVenta.getIdDetalleVenta(),
                        detalleVenta.getCantidad(),
                        detalleVenta.getPrecio(),
                        detalleVenta.getFechaVenta(),
                        detalleVenta.getProducto().getIdProducto(),
                        detalleVenta.getCliente().getIdCliente()))
                .collect(Collectors.toList());

        return new MontoTotalDetalleVentaDTO(montoTotal, detallesVentaDTO);
    }
    // LISTAR TODOS LOS DETALLES DE VENTA
    public List<MontoTotalDetalleVentaDTO> listarTodosLosDetallesVenta() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(cliente -> {
                    double montoTotal = calcularMontoTotal(cliente.getIdCliente());
                    List<DetalleVentaDTO> detallesVenta = detalleVentaRepository.findAll().stream()
                            .filter(detalleVenta -> detalleVenta.getCliente().getIdCliente().equals(cliente.getIdCliente()))
                            .map(detalleVenta -> new DetalleVentaDTO(
                                    detalleVenta.getIdDetalleVenta(),
                                    detalleVenta.getCantidad(),
                                    detalleVenta.getPrecio(),
                                    detalleVenta.getFechaVenta(),
                                    detalleVenta.getProducto().getIdProducto(),
                                    detalleVenta.getCliente().getIdCliente()))
                            .collect(Collectors.toList());
                    return new MontoTotalDetalleVentaDTO(montoTotal, detallesVenta);
                })
                .collect(Collectors.toList());
    }


    // ELIMINAR DETALLE VENTA
    public ResponseDTO eliminarDetalleVenta(Long idDetalleVenta) {
        if (detalleVentaRepository.existsById(idDetalleVenta)) {
            detalleVentaRepository.deleteById(idDetalleVenta);
            return new ResponseDTO("Detalle de venta eliminado correctamente");
        }
        return new ResponseDTO("Detalle de venta no encontrado");
    }

    // ?CONSULTAS PARA DETALLE DE VENTA
    //* CALCULAMOS EL MONTO TOTAL DE LA VENTA O compra que realiza un cliente
    // public double calcularMontoTotal(Long idCliente){
    //     List<DetalleVenta> detallesVenta = detalleVentaRepository.findAll();
    //     double montoTotal = 0;
    //     for (DetalleVenta detalleVenta : detallesVenta) {
    //         if (detalleVenta.getCliente().getIdCliente() == idCliente) {
    //             montoTotal += detalleVenta.getPrecio() * detalleVenta.getCantidad();
    //         }
    //     }
    //     return montoTotal;
    // }
    public Float calcularMontoTotal(Long idCliente) {
        return detalleVentaRepository.calcularTotalComprasPorCliente(idCliente);
    }
    
    
    //! BUSCAR DETALLE VENTA POR FECHA y ver quien compro y que productos compró
    
}
