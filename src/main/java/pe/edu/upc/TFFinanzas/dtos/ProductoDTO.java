package pe.edu.upc.TFFinanzas.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private String nombreProducto;
    private String detalleProducto;
    private Float precio;
    private Integer stock;
    private Long idtipoProducto;
}

