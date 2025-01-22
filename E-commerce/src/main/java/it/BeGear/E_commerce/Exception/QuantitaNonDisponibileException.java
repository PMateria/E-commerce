package it.BeGear.E_commerce.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class QuantitaNonDisponibileException extends RuntimeException {
    public QuantitaNonDisponibileException(String message) {super(message);}
}



