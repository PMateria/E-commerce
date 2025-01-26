package it.BeGear.E_commerce.Dto;


import lombok.Data;

@Data
public class CommentoDTO {
    private String contenuto;
    private int rating;
    private String nome;
    private String descrizione;

    public CommentoDTO(String contenuto, int rating, String nome, String descrizione) {
        this.contenuto= contenuto;
        this.rating= rating;
        this.nome= nome;
        this.descrizione= descrizione;
    }
}
