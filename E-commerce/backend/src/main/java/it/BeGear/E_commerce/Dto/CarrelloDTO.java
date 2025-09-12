package it.BeGear.E_commerce.Dto;
import lombok.Data;
import java.util.List;

@Data
public class CarrelloDTO {
    private int utenteId;
    private List<CarrelloItemDTO> items;
    private double totale;
}
