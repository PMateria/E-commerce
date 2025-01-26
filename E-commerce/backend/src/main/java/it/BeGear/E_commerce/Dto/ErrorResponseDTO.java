package it.BeGear.E_commerce.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorResponseDTO extends Throwable {
    private String apiPath;
    private HttpStatus httpStatus;
    private String errorMessage;
    private LocalDateTime errorTime;
}
