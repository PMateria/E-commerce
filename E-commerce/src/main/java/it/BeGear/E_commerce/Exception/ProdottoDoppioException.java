package it.BeGear.E_commerce.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ProdottoDoppioException extends RuntimeException {
    public ProdottoDoppioException(String message) {
        super(message);
    }
}
