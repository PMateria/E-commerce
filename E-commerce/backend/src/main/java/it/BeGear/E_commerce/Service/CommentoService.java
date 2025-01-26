package it.BeGear.E_commerce.Service;

import it.BeGear.E_commerce.Dto.CommentoDTO;
import it.BeGear.E_commerce.Dto.CommentoDTOMapper;
import it.BeGear.E_commerce.Entity.Commento;
import it.BeGear.E_commerce.Entity.Prodotto;
import it.BeGear.E_commerce.Entity.Utente;
import it.BeGear.E_commerce.Repository.CommentoRepo;
import it.BeGear.E_commerce.Repository.ProdottoRepo;
import it.BeGear.E_commerce.Repository.UtenteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentoService {

    @Autowired
    private CommentoRepo commentoRepo;
    @Autowired
    private UtenteRepo utenteRepo;
    @Autowired
    private ProdottoRepo prodottoRepo;


    //ottengo i commenti relativi ad un prodotto
    public List<CommentoDTO> getCommentiPerProdotto(int prodottoId){
        List<Commento> commenti= commentoRepo.findByProdottoId(prodottoId);
        return commenti.stream().map(CommentoDTOMapper::commentoToDTO).collect(Collectors.toList());
    }

    //aggiungi un commento
    public CommentoDTO aggiungiCommento(int prodottoId, int utenteId, String contenuto, int rating){
        Commento commento= new Commento();
        commento.setContenuto(contenuto);
        commento.setRating(rating);

        Utente utente= utenteRepo.findById(utenteId).orElseThrow(()-> new RuntimeException("Utente non trovato"));
        commento.setUtente(utente);
        Prodotto prodotto= prodottoRepo.findById(prodottoId).orElseThrow(()-> new RuntimeException("Prodotto non trovato"));
        commento.setProdotto(prodotto);
        Commento salvaCommento= commentoRepo.save(commento);

        return CommentoDTOMapper.commentoToDTO(salvaCommento);
    }

    //Get per ottenere un commento tramide l'id dello stesso
    public CommentoDTO getCommentoById(int commentoId){
        Commento commento= commentoRepo.findById(commentoId).orElseThrow(() -> new RuntimeException("Commento non trovato"));
        return CommentoDTOMapper.commentoToDTO(commento);
    }

    //Metodo per eliminare un commento
    public void eliminaCommento(int commentoId){
        Commento commento= commentoRepo.findById(commentoId).orElseThrow(() -> new RuntimeException("Commento non trovato"));
        commentoRepo.delete(commento);
    }

}
