package pe.edu.upc.TFFinanzas.controllers;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.TFFinanzas.dtos.PagoDTO;
import pe.edu.upc.TFFinanzas.dtos.auth.ResponseDTO;
import pe.edu.upc.TFFinanzas.services.PagoService;

@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
public class PagoController {
    private final PagoService pagoService;

    // Registrar Pago
    @PostMapping("/registrar")
    public ResponseEntity<ResponseDTO> registrarPago(@RequestBody PagoDTO pagoDTO) {
        ResponseDTO response = pagoService.registrarPago(pagoDTO);
        if (response.getMessage().equals("Pago registrado correctamente")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Actualizar Pago
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ResponseDTO> actualizarPago(@PathVariable Long id, @RequestBody PagoDTO pagoDTO) {
        ResponseDTO response = pagoService.actualizarPago(id, pagoDTO);
        if (response.getMessage().equals("Pago actualizado correctamente")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // Listar Pagos
    @GetMapping("/listar")
    public ResponseEntity<List<PagoDTO>> listarPagos() {
        List<PagoDTO> pagos = pagoService.listarPagos();
        return new ResponseEntity<>(pagos, HttpStatus.OK);
    }


    // Eliminar Pago
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ResponseDTO> eliminarPago(@PathVariable Long id) {
        ResponseDTO response = pagoService.eliminarPago(id);
        if (response.getMessage().equals("Pago eliminado correctamente")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
