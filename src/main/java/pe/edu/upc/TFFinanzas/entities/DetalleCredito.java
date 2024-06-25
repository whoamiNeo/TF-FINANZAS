package pe.edu.upc.TFFinanzas.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "detalle_credito")
public class DetalleCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleCredito;
    private Float saldoInicial;
    private Float interes;
    private Float renta;
    private Float amortizacion;
    private Float saldoFinal;
    private LocalDate fechaPagoCuota;
    private boolean estadoPago;
    private double mora;
    

    @ManyToOne
    @JoinColumn(name = "idCredito", nullable = false)
    private Credito credito;
    
    @OneToMany(mappedBy = "detalleCredito", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pago> pagos;
}
