package pe.edu.upc.TFFinanzas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upc.TFFinanzas.dtos.TipoProductoDTO;
import pe.edu.upc.TFFinanzas.dtos.auth.ResponseDTO;
import pe.edu.upc.TFFinanzas.entities.TipoProducto;
import pe.edu.upc.TFFinanzas.repositories.TipoProductoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TipoProductoService {
    private final TipoProductoRepository tipoProductoRepository;

    // REGISTRAR TIPO DE PRODUCTO
    public ResponseDTO registrarTipoProducto(TipoProductoDTO tipoProductoDTO) {
        TipoProducto tipoProducto = TipoProducto.builder()
                .descripcion(tipoProductoDTO.getDescripcion())
                .build();
        tipoProductoRepository.save(tipoProducto);
        tipoProductoDTO.setIdTipoProducto(tipoProducto.getIdTipoProducto());
        return new ResponseDTO("tipo producto registrado correctamente");
    }

    // ACTUALIZAR TIPO DE PRODUCTO
    public ResponseDTO actualizarTipoProducto(Long idTipoProducto, TipoProductoDTO tipoProductoDTO) {
        Optional<TipoProducto> tipoProductoExistente = tipoProductoRepository.findById(idTipoProducto);
        if (tipoProductoExistente.isPresent()) {
            TipoProducto tipoProducto = tipoProductoExistente.get();
            tipoProducto.setDescripcion(tipoProductoDTO.getDescripcion());
            tipoProductoRepository.save(tipoProducto);
            return new ResponseDTO("tipo producto actualizado correctamente");
        }
        return new ResponseDTO("tipo producto no encontrado");  
    }

    // LISTAR TODOS LOS TIPOS DE PRODUCTOS
    public List<TipoProductoDTO> listarTodosLosTipoProductos() {
        List<TipoProducto> tipoProductos = tipoProductoRepository.findAll();
        return tipoProductos.stream()
                .map(tipoProducto -> new TipoProductoDTO(
                        tipoProducto.getIdTipoProducto(),
                        tipoProducto.getDescripcion()))
                .collect(Collectors.toList());
    }
    // ELIMINAR TIPO DE PRODUCTO por ID
    public void eliminarTipoProducto(Long idTipoProducto) {
        tipoProductoRepository.deleteById(idTipoProducto);
    }

    // BUSCAR TIPO DE PRODUCTO POR DESCRIPCION
    public ResponseDTO buscarTipoProductoPorDescripcion(String descripcion) {
        Optional<TipoProducto> tipoProducto = tipoProductoRepository.findByDescripcion(descripcion);
        if (tipoProducto.isPresent()) {
            TipoProductoDTO tipoProductoDTO = new TipoProductoDTO(
                    tipoProducto.get().getIdTipoProducto(),
                    tipoProducto.get().getDescripcion());
            return new ResponseDTO("Tipo de producto encontrado: " + tipoProductoDTO.getDescripcion());
        } else {
            return new ResponseDTO("Tipo de producto no encontrado");
        }
    }

    //Buscar Tipo de Producto por ID
    public Optional<TipoProducto> buscarTipoProductoPorId(Long idTipoProducto) {
        return tipoProductoRepository.findById(idTipoProducto);
    }
}
