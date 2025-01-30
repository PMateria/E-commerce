    package it.BeGear.E_commerce.Controller;

    import com.fasterxml.jackson.databind.ObjectMapper;
    import it.BeGear.E_commerce.Costanti.ProdottoCostanti;
    import it.BeGear.E_commerce.Costanti.UtenteCostanti;
    import it.BeGear.E_commerce.Dto.*;
    import it.BeGear.E_commerce.Entity.Categoria;
    import it.BeGear.E_commerce.Entity.Prodotto;
    import it.BeGear.E_commerce.Entity.Utente;
    import it.BeGear.E_commerce.Exception.*;
    import it.BeGear.E_commerce.Repository.CategoriaRepo;
    import it.BeGear.E_commerce.Repository.ProdottoRepo;
    import it.BeGear.E_commerce.Repository.UtenteRepo;
    import it.BeGear.E_commerce.Service.ProdottoService;
    import jakarta.validation.Valid;
    import lombok.AllArgsConstructor;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;


    import java.awt.*;
    import java.io.IOException;
    import java.security.Principal;
    import java.util.Collections;
    import java.util.List;
    import java.util.Map;
    import java.util.Optional;

    @RestController
    @RequestMapping(path = "/gestione_prodotti", produces = {MediaType.APPLICATION_JSON_VALUE})


    public class ProdottoController {
        @Autowired
        private ProdottoService prodottoService;
        @Autowired
        private ProdottoRepo prodottoRepo;
        @Autowired
        private UtenteRepo utenteRepo;
        @Autowired
        private CategoriaRepo categoriaRepo;



        @PostMapping("/creaProdotto")
        public ResponseEntity<ResponseDTO> creaProdotto(
                @RequestPart("prodotto") String prodottoJson, // Ricevi il JSON come stringa
                @RequestParam(value = "immagine", required = false) MultipartFile immagine) {

            // Converti il JSON in un oggetto ProdottoDTO
            ObjectMapper objectMapper = new ObjectMapper();
            ProdottoDTO prodottoDTO;
            try {
                prodottoDTO = objectMapper.readValue(prodottoJson, ProdottoDTO.class);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO("400", "Errore nella conversione del JSON"));
            }

            // Recupera l'utente autenticato
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Utente utente = utenteRepo.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Utente autenticato non trovato"));

            // Crea il prodotto
            ProdottoDTO creaProdotto = prodottoService.creaProdotto(prodottoDTO, utente.getId(), immagine);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseDTO(ProdottoCostanti.STATUS_201, ProdottoCostanti.STATUS_201_MESSAGE));
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


        @GetMapping("/getProdottoById/{id}")
        public ResponseEntity<Object> getProdottoId(@PathVariable int id) {
            try {
                ProdottoDTO prodottoDTO = prodottoService.getProductById(id);
                if (prodottoDTO == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(ProdottoCostanti.STATUS_400, ProdottoCostanti.STATUS_400_MESSAGE + " " + id));
                }
                return ResponseEntity.status(HttpStatus.OK)
                        .body(prodottoDTO);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDTO(ProdottoCostanti.STATUS_500, ProdottoCostanti.STATUS_500_MESSAGE));
            }
        }


        @PutMapping("/modificaProdotto/{id}")
        public ResponseEntity<Object> updateProdotto(@PathVariable int id, @RequestBody ProdottoDTO prodottoDTO, @RequestParam MultipartFile immagine) {
            try {
                ProdottoDTO modificaProdotto = prodottoService.updateProdotto(id, prodottoDTO, immagine );
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