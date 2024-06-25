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
public class DetalleVentaDTO {
    private Long idDetalleVenta;
    private Integer cantidad;
    private Float precio;
    private LocalDate fechaVenta;
    private Long idProducto;
    private Long idCliente;
}
