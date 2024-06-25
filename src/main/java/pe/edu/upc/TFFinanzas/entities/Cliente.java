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
@Table(name = "cliente")
public class Cliente{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;
    private String nombre;
    private String apellido;
    private String correo;
    private Integer numeroDocumento;
    private Integer telefono;
    private Float limiteCredito;
    private String direccion;

    //relacion CLIENTE CON CREDITO 
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Credito> creditos;

    //relacion CLIENTE CON detalleVenta
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<DetalleVenta> detalleVentas;

    // Relación CLIENTE con USUARIO
    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private UserEntity users;
}
