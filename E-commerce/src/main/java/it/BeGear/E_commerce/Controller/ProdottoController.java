package it.BeGear.E_commerce.Controller;

import it.BeGear.E_commerce.Costanti.ProdottoCostanti;
import it.BeGear.E_commerce.Dto.AcquistaProdottoDTO;
import it.BeGear.E_commerce.Dto.FasciaDiPrezzo;
import it.BeGear.E_commerce.Dto.ProdottoDTO;
import it.BeGear.E_commerce.Dto.ResponseDTO;
import it.BeGear.E_commerce.Entity.Prodotto;
import it.BeGear.E_commerce.Exception.ProdottoAssenteException;
import it.BeGear.E_commerce.Exception.QuantitaNonDisponibileException;
import it.BeGear.E_commerce.Exception.SaldoInsufficienteException;
import it.BeGear.E_commerce.Exception.UtenteAssenteException;
import it.BeGear.E_commerce.Repository.ProdottoRepo;
import it.BeGear.E_commerce.Service.ProdottoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path = "/gestione_prodotti", produces = {MediaType.APPLICATION_JSON_VALUE})


public class ProdottoController {
    @Autowired
    private ProdottoService prodottoService;
    @Autowired
    private ProdottoRepo prodottoRepo;
    @Autowired
    WebClient webClient;


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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(ProdottoCostanti.STATUS_500, ProdottoCostanti.STATUS_500_MESSAGE));
            }
            return ResponseEntity.ok(modificaProdotto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(ProdottoCostanti.STATUS_400, ProdottoCostanti.STATUS_400_MESSAGE));
        }
    }

    @DeleteMapping("/cancellaProdotto/{id}")
    public ResponseEntity<ResponseDTO> cancellaProdotto(@PathVariable int id) {
        try {
            prodottoService.deleteProdotto(id);
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

    @PostMapping("/acquistaProdotti")
    public ResponseEntity<ResponseDTO> acquistaProdotti(@RequestBody List<AcquistaProdottoDTO> acquisti,
                                                        @RequestParam int utenteId) {
        try {
            prodottoService.acquistaProdotti(acquisti, utenteId);
            return ResponseEntity.ok(
                    new ResponseDTO(
                            ProdottoCostanti.STATUS_200,
                            "Acquisto completato con successo"
                    )
            );
        } catch (UtenteAssenteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(
                            ProdottoCostanti.STATUS_400,
                            e.getMessage()
                    ));
        } catch (ProdottoAssenteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(
                            ProdottoCostanti.STATUS_400,
                            e.getMessage()
                    ));
        }  catch (SaldoInsufficienteException e) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                    .body(new ResponseDTO(
                            ProdottoCostanti.STATUS_400,
                            e.getMessage()
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(
                            ProdottoCostanti.STATUS_500,
                            ProdottoCostanti.STATUS_500_MESSAGE
                    ));
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
    public ResponseEntity<Object> getProdottiPiuVenduti(@PathVariable String fascia,@RequestParam int limit) {
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
    public ResponseEntity<Object> getProdottiPiVendutiPerTutteLeFasce(@RequestParam int limitPerFascia){
        try{
            Map<FasciaDiPrezzo, List<ProdottoDTO>> risultati =
                    prodottoService.getProdottiPiuVendutiPerTutteLeFasce(limitPerFascia);
            return ResponseEntity.ok(risultati);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(ProdottoCostanti.STATUS_500, ProdottoCostanti.STATUS_500_MESSAGE));
        }
    }

    @PostMapping("/assegnaFornitore")
    public ResponseEntity<ResponseDTO> assegnaFornitore(@RequestParam int codiceFornitore,
                                                        @RequestParam int codiceProdotto) {

        List<Integer> codiceProdottoList = List.of(codiceProdotto);

        // Verifica che il prodotto esista
        List<Prodotto> prodotti = prodottoRepo.findByCodiceProdotto(codiceProdottoList);
        if (prodotti.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Prodotto non trovato con codice: " + codiceProdotto
            );
        }

        // Assegna il fornitore al prodotto
        prodottoRepo.assegnazioneFornitore(codiceProdotto, codiceFornitore);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(ProdottoCostanti.STATUS_200, ProdottoCostanti.STATUS_200_MESSAGE));
    }

    @PostMapping("/assegnaMagazzino")
    public ResponseEntity<ResponseDTO> assegnaMagazzino(@RequestParam int codiceProdotto,
                                                        @RequestParam Long codiceMagazzino) {
        List<Integer> codiceProdottoList = List.of(codiceProdotto);
        List<Prodotto> prodotti = prodottoRepo.findByCodiceProdotto(codiceProdottoList);

        if (prodotti.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Prodotto non trovato con codice: " + codiceProdotto
            );
        }
        ResponseDTO responseDTO = webClient.post()
                .uri("http://localhost:8091/gestione_magazzini/assegnaProdotto",
                        uriBuilder -> uriBuilder
                                .queryParam("codiceProdotto", codiceProdotto)
                                .queryParam("codiceMagazzino", codiceMagazzino)
                                .build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> {
                    if (response.statusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Magazzino non trovato con codice: " + codiceMagazzino
                        ));
                    }
                    return Mono.error(new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Errore nella richiesta"
                    ));
                })
                .onStatus(status -> status.is5xxServerError(), response -> {
                    return Mono.error(new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Errore nel server di gestione magazzini"
                    ));
                })
                .bodyToMono(ResponseDTO.class)
                .block();

        return ResponseEntity.ok(responseDTO);
    }


}