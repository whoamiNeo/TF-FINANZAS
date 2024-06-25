package pe.edu.upc.TFFinanzas.controllers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.TFFinanzas.dtos.CreditoTDO;
import pe.edu.upc.TFFinanzas.services.CreditoService;
import pe.edu.upc.TFFinanzas.dtos.auth.ResponseDTO;
import java.util.List;

@RestController
@RequestMapping("/creditos")
@RequiredArgsConstructor
public class CreditoController {
    private final CreditoService creditoService;

    // REGISTRAR CREDITO
    @PostMapping("/registrar")
    public ResponseEntity<ResponseDTO> registrarCredito(@RequestBody CreditoTDO creditoDTO) {
        ResponseDTO response = creditoService.registrarCredito(creditoDTO);
        if (response.getMessage().equals("Crédito registrado correctamente")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ACTUALIZAR CREDITO
    // @PutMapping("/actualizar/{id}")
    // public ResponseEntity<ResponseDTO> actualizarCredito(@PathVariable Long id, @RequestBody CreditoTDO creditoDTO) {
    //     ResponseDTO response = creditoService.actualizarCredito(id, creditoDTO);
    //     if (response.getMessage().equals("Crédito actualizado correctamente")) {
    //         return new ResponseEntity<>(response, HttpStatus.OK);
    //     } else {
    //         return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    //     }
    // }
    //LISTAR CREDITOS
    // LISTAR TODOS LOS CREDITOS
    @GetMapping("/listar")
    public ResponseEntity<List<CreditoTDO>> listarCreditos() {
        List<CreditoTDO> response = creditoService.listarCreditos();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

      //ELIMINAR CREDITO
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ResponseDTO> eliminarCredito(@PathVariable Long id) {
        ResponseDTO response = creditoService.eliminarCredito(id);
        if (response.getMessage().equals("Crédito eliminado correctamente")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
