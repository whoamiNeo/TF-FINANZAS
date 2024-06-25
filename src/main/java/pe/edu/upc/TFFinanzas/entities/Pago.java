package pe.edu.upc.TFFinanzas.entities;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    private Float monto;
    //enumeracion de tipo de pago
    private LocalDate fechaPago;
    @Enumerated(EnumType.STRING)
    private TipoPagoEnum tipoPago;
    //*relacion pago-credito
    private boolean estadoPago;
    @ManyToOne
    @JoinColumn(name = "idDetalleCredito", nullable = false)
    private DetalleCredito detalleCredito;
}
