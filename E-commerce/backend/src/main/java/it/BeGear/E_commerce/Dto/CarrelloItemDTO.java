package it.BeGear.E_commerce.Dto;


import lombok.Data;

@Data
public class CarrelloItemDTO {
    private int prodottoId;
    private int quantita;
    private double prezzoUnitario;
    private double totaleItem;
}
