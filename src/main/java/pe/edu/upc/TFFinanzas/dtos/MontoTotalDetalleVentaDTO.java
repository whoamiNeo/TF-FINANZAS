package pe.edu.upc.TFFinanzas.dtos;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MontoTotalDetalleVentaDTO {
    private double montoTotal;
    private List<DetalleVentaDTO> detallesVenta;
}
