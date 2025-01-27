package it.BeGear.E_commerce.Controller;

import it.BeGear.E_commerce.Costanti.ProdottoCostanti;
import it.BeGear.E_commerce.Costanti.UtenteCostanti;
import it.BeGear.E_commerce.Dto.FasciaDiPrezzo;
import it.BeGear.E_commerce.Dto.ProdottoDTO;
import it.BeGear.E_commerce.Dto.ResponseDTO;
import it.BeGear.E_commerce.Dto.UtenteDTO;
import it.BeGear.E_commerce.Entity.Prodotto;
import it.BeGear.E_commerce.Exception.ProdottoAssenteException;
import it.BeGear.E_commerce.Exception.ProdottoDoppioException;
import it.BeGear.E_commerce.Exception.QuantitaNonDisponibileException;
import it.BeGear.E_commerce.Exception.UtenteAssenteException;
import it.BeGear.E_commerce.Repository.ProdottoRepo;
import it.BeGear.E_commerce.Service.ProdottoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/gestione_prodotti", produces = {MediaType.APPLICATION_JSON_VALUE})


public class ProdottoController {
    @Autowired
    private ProdottoService prodottoService;
    @Autowired
    private ProdottoRepo prodottoRepo;

    //Crud per la creazione di un prodotto
    @PostMapping("/creaProdotto")
    public ResponseEntity<ResponseDTO> creaProdotto(@Valid @RequestBody ProdottoDTO prodottoDTO) {
        ProdottoDTO creaProdotto = prodottoService.creaProdotto(prodottoDTO);
        if (creaProdotto != null) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseDTO(ProdottoCostanti.STATUS_201, ProdottoCostanti.STATUS_201_MESSAGE));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(ProdottoCostanti.STATUS_400, ProdottoCostanti.STATUS_400_MESSAGE));
        }
    }


    //Leggi prodotti associati ad un utente
    @GetMapping("/leggiProdotti")
    public ResponseEntity<Object> getAllProdotto() {
        try {
            List<ProdottoDTO> prodotti = prodottoService.getAllProdotti();
            return ResponseEntity.status(HttpStatus.OK).body(prodotti);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(ProdottoCostanti.STATUS_400, ProdottoCostanti.STATUS_400_MESSAGE));
        }
    }


    @PutMapping("/modificaProdotto/{id}")
    public ResponseEntity<Object> updateProdotto(@PathVariable int id, @RequestBody ProdottoDTO prodottoDTO) {
        try {
            ProdottoDTO modificaProdotto = prodottoService.updateProdotto(id, prodottoDTO);
            if (modificaProdotto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(ProdottoCostanti.STATUS_500, ProdottoCostanti.STATUS_500_MESSAGE)); // Risposta con corpo nullo
            }
            return ResponseEntity.ok(modificaProdotto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(ProdottoCostanti.STATUS_400, ProdottoCostanti.STATUS_400_MESSAGE));
        }
    }

    @DeleteMapping("/cancellaProdotto/{id}")
    public ResponseEntity<ResponseDTO> cancellaProdotto(@PathVariable int id) {
        try {
            boolean cancellaProdotto = prodottoService.deleteProdotto(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(ProdottoCostanti.STATUS_200, ProdottoCostanti.STATUS_200_MESSAGE));
        } catch (ProdottoAssenteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(ProdottoCostanti.STATUS_400, "Prodotto con id: " + id + " non trovato"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(ProdottoCostanti.STATUS_500, ProdottoCostanti.STATUS_500_MESSAGE));
        }
    }

    // Acquista prodotto
    @GetMapping("/acquistaProdotto/{id}/{quantitaRichiesta}/{utenteId}")
    public ResponseEntity<ResponseDTO> acquistaProdotto(@PathVariable int id,
                                                        @PathVariable int quantitaRichiesta,
                                                        @PathVariable int utenteId) {
        try {
            prodottoService.acquistaProdotto(quantitaRichiesta, id, utenteId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(ProdottoCostanti.STATUS_200, ProdottoCostanti.STATUS_200_MESSAGE));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(ProdottoCostanti.STATUS_500, ProdottoCostanti.STATUS_500_MESSAGE));
        }
    }

    @GetMapping("/filtratiPerSaldo")
    public ResponseEntity<List<ProdottoDTO>> getProdottiFiltratiPerSaldo(Principal principal) {
        try {
            List<ProdottoDTO> prodottiFiltrati = prodottoService.getProdottiFiltratiPerSaldo(principal);
            return ResponseEntity.ok(prodottiFiltrati);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/prodottiPiuVenduti/{fascia}")
    public ResponseEntity<Object> getProdottiPiuVenduti(
            @PathVariable String fascia,
            @RequestParam int limit) {
        try {
            FasciaDiPrezzo fasciaDiPrezzo = FasciaDiPrezzo.valueOf(fascia.toUpperCase());

            List<ProdottoDTO> prodottiPiuVenduti = prodottoService.getProdottiPiuVenduti(fasciaDiPrezzo, limit);
            return ResponseEntity.ok(prodottiPiuVenduti);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(ProdottoCostanti.STATUS_400, "Fascia di prezzo non valida"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(ProdottoCostanti.STATUS_500, ProdottoCostanti.STATUS_500_MESSAGE));
        }
    }

    @GetMapping ("/prodottiPiuVendutiPerTutteLeFasce")
    public ResponseEntity<Object> getProdottiPiuVendutiPerTutteLeFasce(@RequestParam int limitPerFascia){
        try{
            Map<FasciaDiPrezzo, List<ProdottoDTO>> risultati =
                    prodottoService.getProdottiPiuVendutiPerTutteLeFasce(limitPerFascia);
            return ResponseEntity.ok(risultati);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(ProdottoCostanti.STATUS_500, ProdottoCostanti.STATUS_500_MESSAGE));
        }
    }
}