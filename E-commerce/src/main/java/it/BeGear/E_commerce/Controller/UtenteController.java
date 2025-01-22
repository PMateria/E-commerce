package it.BeGear.E_commerce.Controller;

import it.BeGear.E_commerce.Costanti.UtenteCostanti;
import it.BeGear.E_commerce.Dto.ResponseDTO;
import it.BeGear.E_commerce.Dto.UtenteDTO;
import it.BeGear.E_commerce.Entity.Utente;
import it.BeGear.E_commerce.Service.AuthenticationService;
import it.BeGear.E_commerce.Service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/gestione_utenti")

public class UtenteController {

    @Autowired
    private UtenteService utenteService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationService authService;

    @PostMapping("/aggiungiUtente")
    public ResponseEntity<ResponseDTO> aggiungiUtente(@RequestBody Utente utente) {
        try {
            ResponseDTO response = authService.register(utente);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ResponseDTO errorResponse = new ResponseDTO(null, "Errore durante la registrazione: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

        @PostMapping("/login")
        public ResponseEntity<ResponseDTO> authenticate(@RequestBody Utente utente) {
            try {
                ResponseDTO response = authService.authenticate(utente);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ResponseDTO(null, "401 - Autenticazione fallita: " + e.getMessage()));
            }
        }


    @GetMapping("/getUtente/{id}")
    public ResponseEntity<Object> getUtenteId(@PathVariable int id) {
        try {
            UtenteDTO utente = utenteService.getUtenteById(id);
            if (utente == null) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(UtenteCostanti.STATUS_400, UtenteCostanti.STATUS_400_MESSAGE + " " + id));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(utente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(UtenteCostanti.STATUS_500, UtenteCostanti.STATUS_500_MESSAGE));
        }
    }


    @GetMapping("/getUtenti")
    public ResponseEntity<Object> getAllUtenti() {
        try {
            List<UtenteDTO> utenti = utenteService.getAllUtenti();
            if (utenti.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(UtenteCostanti.STATUS_400, UtenteCostanti.STATUS_400_MESSAGE));
            }
            return ResponseEntity.status(HttpStatus.OK).body(utenti);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(UtenteCostanti.STATUS_500, UtenteCostanti.STATUS_500_MESSAGE));
        }
    }

    @PutMapping("/modificaUtente/{id}")
    public ResponseEntity<Object> modificaUtente(@PathVariable int id, @RequestBody UtenteDTO utenteDTO) {
        try {
            UtenteDTO modificaUtente = utenteService.updateUtente(id, utenteDTO);
            if (modificaUtente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(UtenteCostanti.STATUS_404, UtenteCostanti.STATUS_404_MESSAGE + " " + id));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(modificaUtente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(UtenteCostanti.STATUS_500, UtenteCostanti.STATUS_500_MESSAGE));
        }
    }


    @PutMapping("/aggiungiSaldo")
    public ResponseEntity<ResponseDTO> aggiungiSaldo(@RequestParam double somma, Principal principal) {
        try {
            utenteService.aggiungiSaldoWallet(somma, principal.getName());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(UtenteCostanti.STATUS_200, UtenteCostanti.STATUS_200_MESSAGE));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(UtenteCostanti.STATUS_400, e.getMessage()));
        }
    }

    @PutMapping("/sottraiSaldo")
    public ResponseEntity<ResponseDTO> sottraiSaldo(@RequestParam double somma, Principal principal) {
        try {
            boolean sottraisaldo = utenteService.sottraiSaldo(somma, principal.getName());
            if (sottraisaldo) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(UtenteCostanti.STATUS_200, UtenteCostanti.STATUS_200_MESSAGE));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(UtenteCostanti.STATUS_400, UtenteCostanti.STATUS_400_MESSAGE));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(UtenteCostanti.STATUS_500, e.getMessage()));
        }
    }


    @GetMapping("/getSaldo")
    public ResponseEntity<ResponseDTO> getSaldo(Principal principal) {
        try {
            String username = principal.getName();
            double saldo = utenteService.getSaldoWallett(username);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(UtenteCostanti.STATUS_200, "Saldo disponibile: " + saldo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(UtenteCostanti.STATUS_400, e.getMessage()));
        }
    }
}