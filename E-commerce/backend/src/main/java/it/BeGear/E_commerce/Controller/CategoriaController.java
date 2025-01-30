package it.BeGear.E_commerce.Controller;

import it.BeGear.E_commerce.Costanti.ProdottoCostanti;
import it.BeGear.E_commerce.Dto.ProdottoDTO;
import it.BeGear.E_commerce.Dto.ResponseDTO;
import it.BeGear.E_commerce.Entity.Categoria;
import it.BeGear.E_commerce.Exception.CategoriaEsistenteException;
import it.BeGear.E_commerce.Repository.CategoriaRepo;
import it.BeGear.E_commerce.Service.CategoriaService;
import it.BeGear.E_commerce.Service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;


import java.util.List;

@RestController
@RequestMapping("/gestione_categorie")
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;
    @Autowired
    CategoriaRepo categoriaRepo;
    @Autowired
    ProdottoService prodottoService;

    @PostMapping("/creaCategoria")
    public ResponseEntity<Object> creaCategoria(@RequestBody Categoria categoria) {
        try {
            Categoria savedCategoria = categoriaService.createCategoria(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategoria);
        } catch (CategoriaEsistenteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/modificaCategoria/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long id, @RequestBody Categoria dettagliCategoria) {
        Categoria updatedCategoria = categoriaService.modificaCategoria(id, dettagliCategoria);
        return ResponseEntity.ok(updatedCategoria);
    }

    @GetMapping("/ottieniCategorie")
    public ResponseEntity<List<Categoria>> ottieniCategorie(){
        return ResponseEntity.ok(categoriaService.getAllCategorie());
    }

    @GetMapping("/prodottiPerCategoria/{categoriaNome}")
    public ResponseEntity<List<ProdottoDTO>> getProdottiPerCategoria(@PathVariable String categoriaNome) {
        try {
            Categoria categoria = categoriaRepo.findByNome(categoriaNome)
                    .orElseThrow(() -> new RuntimeException("Categoria con nome: " + categoriaNome + " non trovata"));
            List<ProdottoDTO> prodotti = prodottoService.getProdottiByCategoria(categoria);
            return ResponseEntity.ok(prodotti);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<ProdottoDTO>());
        }
    }

    @DeleteMapping("/cancellaCategoria/{id}")
    public ResponseEntity<String> cancellaCategoria(@PathVariable Long id) {
        boolean categoriaEsistente = categoriaService.cancellaCategoria(id);

        if (categoriaEsistente) {
            return ResponseEntity.ok("Categoria cancellata con successo.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria non trovata.");
        }
    }

}
