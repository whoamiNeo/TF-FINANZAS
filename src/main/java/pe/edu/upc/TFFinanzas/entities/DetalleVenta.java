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
@Table(name = "detalleVenta")
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleVenta;

    private Integer cantidad;
    private Float precio;
    private LocalDate fechaVenta;

    //Relacion detalleVenta - producto
    @ManyToOne
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;
    //Relacion detalleVenta con cliente 
    @ManyToOne
    @JoinColumn(name = "idCliente", nullable = false)
    private Cliente cliente;

}
