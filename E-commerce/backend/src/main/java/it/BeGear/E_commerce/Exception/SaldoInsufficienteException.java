package it.BeGear.E_commerce.Exception;

public class SaldoInsufficienteException extends RuntimeException {
    public SaldoInsufficienteException(String message) {
        super(message);
    }
}