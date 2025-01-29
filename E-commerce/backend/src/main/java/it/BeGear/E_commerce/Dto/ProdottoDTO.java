package it.BeGear.E_commerce.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ProdottoDTO {
    private String descrizione;
    private int prezzo;
    private int quantita;
    private int quantitaVenduta;
    private Long categoriaId;

    @Max(value = 100)
    private int sconto;

}
