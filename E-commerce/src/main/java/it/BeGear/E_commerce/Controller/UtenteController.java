package it.BeGear.E_commerce.Controller;

import it.BeGear.E_commerce.Costanti.UtenteCostanti;
import it.BeGear.E_commerce.Dto.LoginDTO;
import it.BeGear.E_commerce.Dto.ResponseDTO;
import it.BeGear.E_commerce.Dto.UtenteDTO;
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

    //crea Utente
    @PostMapping("/aggiungiUtente")
    public ResponseEntity<ResponseDTO> aggiungiUtente(@RequestBody UtenteDTO utenteDTO) {
        try {
            String hashPwd = passwordEncoder.encode(utenteDTO.getPassword());
            utenteDTO.setPassword(hashPwd);
            UtenteDTO utenteCreato = utenteService.registraUtente(utenteDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO(UtenteCostanti.STATUS_201, UtenteCostanti.STATUS_200_MESSAGE + " " + utenteCreato.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(UtenteCostanti.STATUS_500, UtenteCostanti.STATUS_500_MESSAGE));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginDTO loginDTO){
        try {
            boolean autenticato= utenteService.authenticateUser(loginDTO.getUsername(), loginDTO.getPassword());
            if(autenticato){
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(UtenteCostanti.STATUS_200, UtenteCostanti.STATUS_200_MESSAGE));
            }else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO(UtenteCostanti.STATUS_200, UtenteCostanti.STATUS_200_MESSAGE));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(UtenteCostanti.STATUS_500, UtenteCostanti.STATUS_500_MESSAGE));
        }
    }

    //Restituisco un oggetto generico in modo da poter avere sia l'oggetto che le eccezzioni quindi sia ResponseDTO che UserDTO
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
            // Gestisci eventuali errori generali
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

// Api per saldo
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
            // Ottieni l'username dall'oggetto Principal
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