package it.BeGear.E_commerce.Service;

import it.BeGear.E_commerce.Entity.Categoria;
import it.BeGear.E_commerce.Exception.CategoriaEsistenteException;
import it.BeGear.E_commerce.Exception.ResourceNotFoundException;
import it.BeGear.E_commerce.Repository.CategoriaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    CategoriaRepo categoriaRepo;



    public Categoria createCategoria(Categoria categoria) {
        if (categoriaRepo.existsByNome(categoria.getNome())) {
            throw new CategoriaEsistenteException("Categoria esistente");
        }
        return categoriaRepo.save(categoria);
    }


    public Categoria modificaCategoria(Long id, Categoria modificaCategoria){
        Categoria categoria = categoriaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria non trovata con id: " + id));
        categoria.setNome(modificaCategoria.getNome());
        return categoriaRepo.save(categoria);
    }

    public List<Categoria> getAllCategorie(){
        return categoriaRepo.findAll();
    }

    public boolean cancellaCategoria(Long id) {
        Optional<Categoria> categoria = categoriaRepo.findById(id);
        if (categoria.isPresent()) {
            categoriaRepo.deleteById(id);
            return true;  // La categoria è stata cancellata con successo
        }
        return false;  // La categoria non è stata trovata
    }

}
