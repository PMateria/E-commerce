package it.BeGear.E_commerce.Dto;

import it.BeGear.E_commerce.Entity.Utente;

public class UtenteDtoMapper {

    public static UtenteDTO utenteDto (Utente utente, UtenteDTO utenteDTO) {
        utenteDTO.setNome(utente.getNome());
        utenteDTO.setEmail(utente.getEmail());
        utenteDTO.setUsername(utente.getUsername());
        utenteDTO.setRuolo(utente.getRuolo());
        utenteDTO.setSaldoWallett(utente.getSaldoWallett());
        utenteDTO.setPassword(utente.getPassword());
        utenteDTO.setCognome(utente.getCognome());
        return utenteDTO;
    }

    public static Utente DTOToUtente (UtenteDTO utenteDto, Utente utente) {
        utente.setNome(utenteDto.getNome());
        utente.setEmail(utenteDto.getEmail());
        utente.setUsername(utenteDto.getUsername());
        utente.setRuolo(utenteDto.getRuolo());
        utente.setSaldoWallett(utente.getSaldoWallett());
        utente.setPassword(utenteDto.getPassword());
        utente.setCognome(utenteDto.getCognome());
        return utente;
    }
}
