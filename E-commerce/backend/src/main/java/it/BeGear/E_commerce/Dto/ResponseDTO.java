package it.BeGear.E_commerce.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {
    private String responseStatus;
    private String responseMessage;
    private String token;

    public ResponseDTO(String responseStatus, String responseMessage) {
        this.responseStatus = responseStatus;
        this.responseMessage = responseMessage;
    }
}
