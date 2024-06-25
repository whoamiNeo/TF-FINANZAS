package pe.edu.upc.TFFinanzas.entities;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "credito")
public class Credito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCredito;

    private Float monto;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private  Boolean estado;
    //ENUMERACIONES
    //TIPO DE CREDITO (corto y largo plazo)
    @Enumerated(EnumType.STRING)
    private TipoCreditoEnum TipoCredito;
    //TIPO DE INTERES (efectivo, nominal, anualidad simple)
    @Enumerated(EnumType.STRING)
    private TipoInteresEnum tipoInteres;
    //NUMERO DE PERIODOS POR PLAZO DE GRACIA
    @Enumerated(EnumType.STRING)
    private PlazoGraciaEnum plazoGracia;
    //NUMERO DE DIAS POR PERIODO DE PAGO
    @Enumerated(EnumType.STRING)
    private  NumeroDiasCuotaEnum numeroDiasCuota;


    

    //*RELACION credito-cliente
    @ManyToOne
    @JoinColumn(name = "idCliente", nullable = false)
    private Cliente cliente;

    //*RELACION credito-pago
    @OneToMany(mappedBy = "credito", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DetalleCredito> detalleCreditos;
}

