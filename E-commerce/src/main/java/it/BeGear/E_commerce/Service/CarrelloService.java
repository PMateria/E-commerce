package it.BeGear.E_commerce.Service;

import it.BeGear.E_commerce.Dto.CarrelloDTO;
import it.BeGear.E_commerce.Dto.CarrelloDTOMapper;
import it.BeGear.E_commerce.Entity.Carrello;
import it.BeGear.E_commerce.Entity.CarrelloItem;
import it.BeGear.E_commerce.Entity.Prodotto;
import it.BeGear.E_commerce.Entity.Utente;
import it.BeGear.E_commerce.Exception.CarrelloException;
import it.BeGear.E_commerce.Exception.ProdottoAssenteException;
import it.BeGear.E_commerce.Exception.QuantitaNonDisponibileException;
import it.BeGear.E_commerce.Exception.UtenteAssenteException;
import it.BeGear.E_commerce.Repository.CarrelloRepo;
import it.BeGear.E_commerce.Repository.ProdottoRepo;
import it.BeGear.E_commerce.Repository.UtenteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarrelloService {
    @Autowired
    private UtenteRepo utenteRepo;

    @Autowired
    private CarrelloRepo carrelloRepo;

    @Autowired
    private ProdottoRepo prodottoRepo;


    public CarrelloDTO aggiungiAlCarrello(int utenteId, int prodottoId, int quantita) {
        Utente utente = utenteRepo.findById(utenteId)
                .orElseThrow(() -> new UtenteAssenteException("Utente non trovato"));

        Prodotto prodotto = prodottoRepo.findById(prodottoId)
                .orElseThrow(() -> new ProdottoAssenteException("Prodotto non trovato"));

        if (prodotto.getQuantita() < quantita) {
            throw new QuantitaNonDisponibileException("Quantità richiesta non disponibile");
        }
        // Carrello contiene informazioni specifiche come (idCarrello, totale, utenteId)
        final Carrello carrello = carrelloRepo.findByUtente(utente)
                .orElseGet(() -> {
                    Carrello nuovoCarrello = new Carrello();
                    nuovoCarrello.setUtente(utente);
                    return nuovoCarrello;
                });

        // Verifica se il prodotto è già nel carrello
       //  CarrelloItem contiene informazioni generali riguardanti il carrello (prezzo_unitario, quantità, numero di item ecc)
        CarrelloItem item = null;
        for (CarrelloItem carrelloItem : carrello.getItems()) {
            if (carrelloItem.getProdotto().getId() == prodottoId) {
                item = carrelloItem;
                break;
            }
        }

        if (item == null) {
            item = new CarrelloItem();
            item.setProdotto(prodotto);
            carrello.getItems().add(item);
        }

        item.setQuantita(item.getQuantita() + quantita);
        item.setPrezzoUnitario(prodotto.getPrezzo() * (1 - prodotto.getSconto() / 100.0));
        item.setTotaleItem(item.getPrezzoUnitario() * item.getQuantita());

        carrello.setTotale(calcolaTotaleCarrello(carrello));

        Carrello savedCarrello = carrelloRepo.save(carrello);
        return CarrelloDTOMapper.carrelloToDTO(savedCarrello);
    }

    private double calcolaTotaleCarrello(Carrello carrello) {
        double totale = 0.0;
            for (CarrelloItem item : carrello.getItems()) {
            totale += item.getTotaleItem();
        }
        return totale;
    }

    //Visualizza i prodotti aggiunti al carrello dell'utente
    public CarrelloDTO getCarrello(int utenteId) {
        Utente utente = utenteRepo.findById(utenteId)
                .orElseThrow(() -> new UtenteAssenteException("Utente non trovato"));
        Carrello carrello = carrelloRepo.findByUtente(utente)
                .orElseThrow(() -> new CarrelloException("Carrello non trovato"));
        return CarrelloDTOMapper.carrelloToDTO(carrello);
    }


    //elimina prodotto dal carrello
    public CarrelloDTO rimuoviDalCarrello(int utenteId, int prodottoId) {
        Utente utente = utenteRepo.findById(utenteId)
                .orElseThrow(() -> new UtenteAssenteException("Utente non trovato"));

        Carrello carrello = carrelloRepo.findByUtente(utente)
                .orElseThrow(() -> new CarrelloException("Carrello non trovato"));

        CarrelloItem item = null;

        for (CarrelloItem carrelloItem : carrello.getItems()) {
            if (carrelloItem.getProdotto().getId() == prodottoId) {
                item = carrelloItem;
                break;
            }
        }
        if (item == null) {
            throw new CarrelloException("Prodotto non trovato nel carrello");
        }

        if(item.getQuantita()>1){
            item.setQuantita(item.getQuantita()-1);
            item.setTotaleItem(item.getPrezzoUnitario()* item.getQuantita());
        }else {
            carrello.getItems().remove(item);
        }

        carrello.setTotale(carrello.getItems().stream()
                .mapToDouble(CarrelloItem::getTotaleItem)
                .sum());
        carrello = carrelloRepo.save(carrello);
        return CarrelloDTOMapper.carrelloToDTO(carrello);
    }

}