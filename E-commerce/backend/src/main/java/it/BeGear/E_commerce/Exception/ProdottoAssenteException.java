package it.BeGear.E_commerce.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProdottoAssenteException extends RuntimeException {

    public ProdottoAssenteException(String message) {
        super(message);
    }

}
