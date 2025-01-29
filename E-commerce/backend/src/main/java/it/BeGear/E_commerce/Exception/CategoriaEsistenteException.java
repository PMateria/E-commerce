package it.BeGear.E_commerce.Exception;

public class CategoriaEsistenteException extends RuntimeException {

    public CategoriaEsistenteException(String message) {
        super(message);
    }

    public CategoriaEsistenteException(String message, Throwable cause) {
        super(message, cause);
    }
}