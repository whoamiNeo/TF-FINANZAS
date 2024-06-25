package pe.edu.upc.TFFinanzas.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoProductoDTO {
    private Long idTipoProducto;
    private String descripcion;
}
