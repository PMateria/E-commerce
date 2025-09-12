package it.BeGear.E_commerce.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)

public class UtenteAssenteException extends RuntimeException{
    public UtenteAssenteException(String message){
            super(message);
        }
    }
