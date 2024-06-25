package pe.edu.upc.TFFinanzas.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.TFFinanzas.dtos.TipoProductoDTO;
import pe.edu.upc.TFFinanzas.services.TipoProductoService;
import pe.edu.upc.TFFinanzas.dtos.auth.ResponseDTO;
import java.util.List;

@RestController
@RequestMapping("/tipo-productos")
@RequiredArgsConstructor
public class TipoProductoController {
    private final TipoProductoService tipoProductoService;

    // REGISTRAR TIPO PRODUCTO
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarTipoProducto(@RequestBody TipoProductoDTO tipoProductoDTO) {
        try {
            return new ResponseEntity<>(tipoProductoService.registrarTipoProducto(tipoProductoDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO("Error en el registro"), HttpStatus.BAD_REQUEST);
        }
    }

    // ACTUALIZAR TIPO PRODUCTO
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarTipoProducto(@PathVariable Long id, @RequestBody TipoProductoDTO tipoProductoDTO) {
        try {
            return new ResponseEntity<>(tipoProductoService.actualizarTipoProducto(id, tipoProductoDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO("Error al actualizar el tipo de producto"), HttpStatus.BAD_REQUEST);
        }
    }

    // LISTAR TODOS LOS TIPOS DE PRODUCTOS
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/listar")
    public ResponseEntity<List<TipoProductoDTO>> listarTodosLosTipoProductos() {
        List<TipoProductoDTO> tipoProductos = tipoProductoService.listarTodosLosTipoProductos();
        return new ResponseEntity<>(tipoProductos, HttpStatus.OK);
    }

    // ELIMINAR TIPO PRODUCTO
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarTipoProducto(@PathVariable Long id) {
        tipoProductoService.eliminarTipoProducto(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //BUSCAR POR TIPO PRODUCTO POR DESCRIPCION
    ///CREO QUE ESTO ES INNCESARIO 
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/buscar/{descripcion}")
    public ResponseEntity<?> buscarTipoProductoPorDescripcion(@PathVariable String descripcion) {
        try {
            return new ResponseEntity<>(tipoProductoService.buscarTipoProductoPorDescripcion(descripcion), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO("Tipo de producto no encontrado"), HttpStatus.BAD_REQUEST);
        }
    }
}
