package pe.edu.upc.TFFinanzas.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.TFFinanzas.entities.NumeroDiasCuotaEnum;
import pe.edu.upc.TFFinanzas.entities.PlazoGraciaEnum;
import pe.edu.upc.TFFinanzas.entities.TipoCreditoEnum;
import pe.edu.upc.TFFinanzas.entities.TipoInteresEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditoTDO {
    private Long idCredito;
    //Monto es el monto total que el cliente llevando en productos, que se calculó en la entidad detalle venta 
    private Float monto;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean estado;
    private TipoCreditoEnum tipoCredito;
    private TipoInteresEnum tipoInteres;
    private PlazoGraciaEnum plazoGracia;
    private NumeroDiasCuotaEnum numeroDiasCuota;
    private Long idCliente;
    
}
