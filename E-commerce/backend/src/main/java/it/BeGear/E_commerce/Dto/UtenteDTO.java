package it.BeGear.E_commerce.Dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UtenteDTO {
    private String nome;
    private String cognome;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(it|com)$", message = "L'email deve contenere un dominio valido (.it o .com)")
    private String email;
    private String ruolo;
    private String username;
    private String password;
    private double saldoWallett;

}
