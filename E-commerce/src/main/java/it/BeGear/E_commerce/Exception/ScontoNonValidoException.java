package it.BeGear.E_commerce.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Sconto non valido. Deve essere compreso tra 1 e 100")

public class ScontoNonValidoException extends RuntimeException {
    public ScontoNonValidoException(String message) {
        super(message);
    }
}
