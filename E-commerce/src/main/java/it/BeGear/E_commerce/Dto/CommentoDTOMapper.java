package it.BeGear.E_commerce.Dto;

import it.BeGear.E_commerce.Entity.CarrelloItem;
import it.BeGear.E_commerce.Entity.Commento;

public class CommentoDTOMapper {

    public static CommentoDTO commentoToDTO(Commento commento) {
        return new CommentoDTO(
                commento.getContenuto(),
                commento.getRating(),
                commento.getUtente().getNome(),
                commento.getProdotto().getDescrizione()
        );
    }
}
