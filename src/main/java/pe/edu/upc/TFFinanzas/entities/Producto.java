package pe.edu.upc.TFFinanzas.entities;

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
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    private String nombreProducto;
    private String detalleProducto;
    private Float precio;
    private Integer stock;

    //Relacion producto - tipoProducto
    @ManyToOne
    @JoinColumn(name = "idTipoProducto", nullable = false)
    private TipoProducto tipoProducto;

    //Relacion producto - detalleVenta
    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    private List<DetalleVenta> detalleVentas;
}
