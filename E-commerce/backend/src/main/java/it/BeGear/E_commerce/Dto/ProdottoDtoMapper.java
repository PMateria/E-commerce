package it.BeGear.E_commerce.Dto;

import it.BeGear.E_commerce.Entity.Prodotto;

public class ProdottoDtoMapper {

    public static ProdottoDTO prodottoToDTO (Prodotto prodotto, ProdottoDTO prodottoDTO) {
        prodottoDTO.setDescrizione(prodotto.getDescrizione());
        prodottoDTO.setQuantita(prodotto.getQuantita());
        prodottoDTO.setQuantitaVenduta(prodotto.getQuantitaVenduta());
        prodottoDTO.setPrezzo(prodotto.getPrezzo());
        prodottoDTO.setSconto(prodotto.getSconto());
        prodottoDTO.setImmagineUrl(prodotto.getImmagineUrl());
        return prodottoDTO;
    }

    public static Prodotto DTOToProdotto (ProdottoDTO prodottoDTO, Prodotto prodotto) {
        prodotto.setDescrizione(prodottoDTO.getDescrizione());
        prodotto.setQuantita(prodottoDTO.getQuantita());
        prodotto.setQuantitaVenduta(prodottoDTO.getQuantitaVenduta());
        prodotto.setPrezzo(prodottoDTO.getPrezzo());
        prodotto.setSconto(prodottoDTO.getSconto());
        prodotto.setImmagineUrl(prodottoDTO.getImmagineUrl());
        return prodotto;
    }

}
