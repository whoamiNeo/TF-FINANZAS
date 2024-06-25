package pe.edu.upc.TFFinanzas.controllers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.TFFinanzas.dtos.ProductoDTO;
import pe.edu.upc.TFFinanzas.services.ProductoService;
import pe.edu.upc.TFFinanzas.dtos.auth.ResponseDTO;

import java.util.List;


@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;
    //REGISTRAR PRODUCTO
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarProducto(@RequestBody ProductoDTO productoDTO) {
        try {
            return new ResponseEntity<>(productoService.registrarProducto(productoDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO("Error en el registro"), HttpStatus.BAD_REQUEST);
        }
    }
    //ACTUALIZAR PRODUCTO
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
    
        try {
            return new ResponseEntity<>(productoService.actualizarProducto(id, productoDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO("Error al actualizar Producto"), HttpStatus.NOT_FOUND);
        }
    }
    //LISTAR TODOS LOS PRODUCTOS
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/listar")
    public ResponseEntity<List<ProductoDTO>> listarTodosLosProductos() {
        List<ProductoDTO> productos = productoService.listarTodosLosProductos();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    //BUSCAR PRODUCTO POR NOMBRE
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<ProductoDTO>> buscarPorNombre(@PathVariable String nombre) {
        List<ProductoDTO> productos = productoService.buscarPorNombre(nombre);
        if (productos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

     //ELIMINAR PRODUCTO
     // ! FALTA REVISAR BIEN ESTE METODO
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        ResponseDTO response = productoService.eliminarProducto(id);
        if (response.getMessage().equals("Producto no encontrado")) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
