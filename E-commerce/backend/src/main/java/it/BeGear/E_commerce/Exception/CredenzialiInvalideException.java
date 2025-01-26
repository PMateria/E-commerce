package it.BeGear.E_commerce.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Credenziali non valide")
public class CredenzialiInvalideException extends RuntimeException {
    public CredenzialiInvalideException(String message) {
        super(message);
    }
}