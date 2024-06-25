package pe.edu.upc.TFFinanzas.dtos;

import java.time.LocalDate;

import java.util.List;

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
public class CretidoLargoPlazoDTO {
    private Long idCredito;
    //Monto total a pagar
    private Float monto;
    private Float renta;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<LocalDate> fechasDePago;
    private Boolean estado;
    private NumeroDiasCuotaEnum numeroDiasCuota;
    private PlazoGraciaEnum plazoGracia;
    private TipoCreditoEnum tipoCredito;
    private TipoInteresEnum tipoInteres;
    private Long idCliente;
}
