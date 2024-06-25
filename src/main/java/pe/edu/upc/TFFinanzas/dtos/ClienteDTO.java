package pe.edu.upc.TFFinanzas.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    private String nombre;
    private String apellido;
    private String correo;
    private Integer numeroDocumento;
    private Integer telefono;
    private Float limiteCredito;
    private String direccion;
    private Long idUsuario;
}
