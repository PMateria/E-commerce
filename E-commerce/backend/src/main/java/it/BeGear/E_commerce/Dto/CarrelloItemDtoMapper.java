package it.BeGear.E_commerce.Dto;

import it.BeGear.E_commerce.Entity.CarrelloItem;

public class CarrelloItemDtoMapper {

    public static CarrelloItemDTO itemToDTO(CarrelloItem item, CarrelloItemDTO itemDTO) {
        itemDTO = new CarrelloItemDTO();
        itemDTO.setProdottoId(item.getProdotto().getId());
        itemDTO.setQuantita(item.getQuantita());
        itemDTO.setPrezzoUnitario(item.getPrezzoUnitario());
        itemDTO.setTotaleItem(item.getTotaleItem());
        return itemDTO;
    }


    public static CarrelloItem DTOToItem(CarrelloItemDTO itemDTO, CarrelloItem item) {
        item.setQuantita(itemDTO.getQuantita());
        item.setPrezzoUnitario(itemDTO.getPrezzoUnitario());
        item.setTotaleItem(itemDTO.getTotaleItem());
        return item;
    }
}