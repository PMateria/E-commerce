package it.BeGear.E_commerce.Dto;

import it.BeGear.E_commerce.Entity.Carrello;

import java.util.stream.Collectors;

public class CarrelloDTOMapper {


    public static CarrelloDTO carrelloToDTO(Carrello carrello) {
        CarrelloDTO dto = new CarrelloDTO();
        dto.setUtenteId(carrello.getUtente().getId());
        dto.setItems(carrello.getItems().stream()
                .map(item -> {
                    CarrelloItemDTO itemDTO = new CarrelloItemDTO();
                    return CarrelloItemDtoMapper.itemToDTO(item, itemDTO);
                })
                .collect(Collectors.toList()));
        dto.setTotale(carrello.getTotale());
        return dto;
    }

    public static Carrello DTOToCarrello (CarrelloDTO carrelloDTO, Carrello carrello) {
        carrello.setTotale(carrelloDTO.getTotale());
        return carrello;
    }
}
