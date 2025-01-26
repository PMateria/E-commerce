package it.BeGear.E_commerce.Dto;

import lombok.Data;

@Data
public class ProdottoCarrelloDTO {
    private Long prodottoId;
    private String nome;
    private double prezzo;
    private int quantita;
}
