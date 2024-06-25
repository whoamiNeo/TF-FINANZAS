package pe.edu.upc.TFFinanzas.dtos;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCreditoDTO {
    private Long idDetalleCredito;
    private Float saldoInicial;
    private Float interes;
    private Float renta;
    private Float amortizacion;
    private Float saldoFinal;
    private LocalDate fechaPagoCuota;
    private boolean estadoPago;
    private Double mora;
    private Long idCredito;
}
