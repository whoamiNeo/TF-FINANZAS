package pe.edu.upc.TFFinanzas.dtos;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.TFFinanzas.entities.TipoPagoEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTO {
    private Long idPago;
    private Float monto;
    private LocalDate fechaPago;
    private TipoPagoEnum tipoPago;
    private boolean estadoPago;
    private Long idDetalleCredito;
}