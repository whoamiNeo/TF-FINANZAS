package pe.edu.upc.TFFinanzas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.TFFinanzas.dtos.DetalleCreditoDTO;
//import pe.edu.upc.TFFinanzas.dtos.DetalleCreditoResponseDTO;
import pe.edu.upc.TFFinanzas.services.DetalleCreditoService;

import java.util.List;

@RestController
@RequestMapping("/detallesCredito")
@RequiredArgsConstructor
public class DetalleCreditoController {
    private final DetalleCreditoService detalleCreditoService;

    @GetMapping("/listar/{idCredito}")
    public ResponseEntity<List<DetalleCreditoDTO>> listarDetallesPorCredito(@PathVariable Long idCredito) {
        List<DetalleCreditoDTO> detalles = detalleCreditoService.listarDetallesPorCredito(idCredito);
        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }

    // @GetMapping("/listar/{idCredito}")
    // public ResponseEntity<DetalleCreditoResponseDTO> listarDetallesPorCredito(@PathVariable Long idCredito) {
    //     DetalleCreditoResponseDTO response = detalleCreditoService.listarDetallesPorCredito(idCredito);
    //     return new ResponseEntity<>(response, HttpStatus.OK);
    // }
}
