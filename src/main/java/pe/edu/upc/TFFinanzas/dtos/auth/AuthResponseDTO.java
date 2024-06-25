package pe.edu.upc.TFFinanzas.dtos.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO extends ResponseDTO {
    private String token;

    @Builder
    public AuthResponseDTO(String message, String token) {
        super(message);
        this.token = token;
    }
}
