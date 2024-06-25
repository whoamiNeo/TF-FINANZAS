package pe.edu.upc.TFFinanzas.dtos.auth;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {
    private String message;

    public ResponseDTO() {
        this.message = "OK";
    }

    public ResponseDTO(String message) {
        this.message = message;
    }
}
