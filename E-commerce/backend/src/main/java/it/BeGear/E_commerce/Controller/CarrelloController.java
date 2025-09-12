package it.BeGear.E_commerce.Controller;

import it.BeGear.E_commerce.Costanti.CarrelloCostanti;
import it.BeGear.E_commerce.Dto.CarrelloDTO;
import it.BeGear.E_commerce.Dto.ResponseDTO;
import it.BeGear.E_commerce.Service.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "gestione_carrelli")
public class CarrelloController {

    @Autowired
    CarrelloService carrelloService;

    //Aggiunta di un prodotto al carrello
    @PostMapping("/aggiungi/{utenteId}/{prodottoId}/{quantita}")
    public ResponseEntity<Object> aggiungiAlCarrello(@PathVariable int utenteId, @PathVariable int prodottoId, @PathVariable int quantita) {
        try {
            CarrelloDTO carrelloDTO = carrelloService.aggiungiAlCarrello(utenteId, prodottoId, quantita);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(CarrelloCostanti.STATUS_201, CarrelloCostanti.STATUS_201_MESSAGE));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(CarrelloCostanti.STATUS_400, e.getMessage()));
        }
    }

    //Visualizza il carrello dell'utente
    @GetMapping("/leggiCarrello/{utenteId}")
    public ResponseEntity<Object> getCarrello(@PathVariable int utenteId) {
        try {
            CarrelloDTO carrelloDTO = carrelloService.getCarrello(utenteId);
            return ResponseEntity.status(HttpStatus.OK).body(carrelloDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(CarrelloCostanti.STATUS_400, e.getMessage()));
        }
    }

    //Rimuovi un prodotto dal carrello
    @DeleteMapping("/rimuovi/{utenteId}/{prodottoId}")
    public ResponseEntity<ResponseDTO> rimuoviDalCarrello(@PathVariable int utenteId, @PathVariable int prodottoId) {
        try {
            CarrelloDTO carrelloDTO = carrelloService.rimuoviDalCarrello(utenteId, prodottoId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(CarrelloCostanti.STATUS_200, CarrelloCostanti.STATUS_200_MESSAGE));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(CarrelloCostanti.STATUS_400, e.getMessage()));
        }
    }

}
