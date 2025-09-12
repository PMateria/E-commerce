package it.BeGear.E_commerce.Controller;


import it.BeGear.E_commerce.Dto.CommentoDTO;
import it.BeGear.E_commerce.Service.CommentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commenti")
public class CommentiController {
    @Autowired
    private CommentoService commentoService;

    @PostMapping("/aggiungiCommento/{prodottoId}/{utenteId}")
    public ResponseEntity<CommentoDTO> aggiungiCommento(@PathVariable int prodottoId, @PathVariable int utenteId,@RequestBody CommentoDTO request){
        CommentoDTO commentoDTO = commentoService.aggiungiCommento(prodottoId, utenteId, request.getContenuto(), request.getRating());
        return ResponseEntity.ok(commentoDTO);
    }

    @GetMapping("/commentiPerProdotto/{prodottoId}")
    public ResponseEntity<List<CommentoDTO>> getCommentiPerProdotto(@PathVariable int prodottoId){
        List<CommentoDTO> commenti= commentoService.getCommentiPerProdotto(prodottoId);
        return ResponseEntity.ok(commenti);
    }

    // Ottengo un solo commento
    @GetMapping("/commenti/{commentoId}")
    public ResponseEntity<CommentoDTO> getCommento (@PathVariable int commentoId){
        CommentoDTO commentoDTO= commentoService.getCommentoById(commentoId);
        return ResponseEntity.ok(commentoDTO);
    }

    @DeleteMapping("/cancellaCommenti/{commentoId}")
    public ResponseEntity<String> eliminaCommento (@PathVariable int commentoId){
        commentoService.eliminaCommento(commentoId);
        return ResponseEntity.ok("Commento eliminato con successo");
    }
}
