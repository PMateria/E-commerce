package it.BeGear.E_commerce.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {
    private String responseStatus;
    private String responseMessage;

}
