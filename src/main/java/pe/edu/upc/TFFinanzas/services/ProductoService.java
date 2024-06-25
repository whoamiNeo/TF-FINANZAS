package pe.edu.upc.TFFinanzas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upc.TFFinanzas.dtos.ProductoDTO;
import pe.edu.upc.TFFinanzas.entities.Producto;
import pe.edu.upc.TFFinanzas.entities.TipoProducto;
import pe.edu.upc.TFFinanzas.repositories.ProductoRespository;
import pe.edu.upc.TFFinanzas.repositories.TipoProductoRepository;
import pe.edu.upc.TFFinanzas.dtos.auth.ResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRespository productoRespository;
    private final TipoProductoRepository tipoProductoRepository;

    // REGISTRAR PRODUCTO
    public ResponseDTO registrarProducto(ProductoDTO productoDTO) {
        Optional<TipoProducto> tipoProductoOpt = tipoProductoRepository.findById(productoDTO.getIdtipoProducto());
        if (!tipoProductoOpt.isPresent()) {
            return new ResponseDTO("Tipo de producto no encontrado");
        }

        TipoProducto tipoProducto = tipoProductoOpt.get();
        Producto producto = new Producto();
        producto.setNombreProducto(productoDTO.getNombreProducto());
        producto.setDetalleProducto(productoDTO.getDetalleProducto());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setTipoProducto(tipoProducto);

        productoRespository.save(producto);
        return new ResponseDTO("Producto registrado correctamente");
    }

    // ACTUALIZAR PRODUCTO
    public ResponseDTO actualizarProducto(Long idProducto, ProductoDTO productoDTO) {
        Optional<Producto> productoOpt = productoRespository.findById(idProducto);
        if (!productoOpt.isPresent()) {
            return new ResponseDTO("Producto no encontrado");
        }

        Optional<TipoProducto> tipoProductoOpt = tipoProductoRepository.findById(productoDTO.getIdtipoProducto());
        if (!tipoProductoOpt.isPresent()) {
            return new ResponseDTO("Tipo de producto no encontrado");
        }

        Producto producto = productoOpt.get();
        TipoProducto tipoProducto = tipoProductoOpt.get();
        producto.setNombreProducto(productoDTO.getNombreProducto());
        producto.setDetalleProducto(productoDTO.getDetalleProducto());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setTipoProducto(tipoProducto);

        productoRespository.save(producto);
        return new ResponseDTO("Producto actualizado correctamente");
    }
    // LISTAR TODOS LOS PRODUCTOS
    public List<ProductoDTO> listarTodosLosProductos() {
        List<Producto> productos = productoRespository.findAll();
        return productos.stream()
                .map(producto -> new ProductoDTO(
                        producto.getNombreProducto(),
                        producto.getDetalleProducto(),
                        producto.getPrecio(),
                        producto.getStock(),
                        producto.getTipoProducto().getIdTipoProducto()))
                .collect(Collectors.toList());
    }
    // BUSCAR PRODUCTO POR NOMBRE
    public List<ProductoDTO> buscarPorNombre(String nombre) {
        List<Producto> productos = productoRespository.findByNombreProducto(nombre);
        return productos.stream()
                .map(producto -> new ProductoDTO(
                        producto.getNombreProducto(),
                        producto.getDetalleProducto(),
                        producto.getPrecio(),
                        producto.getStock(),
                        producto.getTipoProducto().getIdTipoProducto()))
                .collect(Collectors.toList());
    }
    // ELIMINAR PRODUCTO
    public ResponseDTO eliminarProducto(Long idProducto) {
        if (productoRespository.existsById(idProducto)) {
            productoRespository.deleteById(idProducto);
            return new ResponseDTO("Producto eliminado correctamente");
        }
        return new ResponseDTO("Producto no encontrado");
    }
    
}
