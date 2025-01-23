package it.BeGear.E_commerce.Service;

import it.BeGear.E_commerce.Dto.FasciaDiPrezzo;
import it.BeGear.E_commerce.Dto.ProdottoDTO;
import it.BeGear.E_commerce.Dto.ProdottoDtoMapper;
import it.BeGear.E_commerce.Entity.Prodotto;
import it.BeGear.E_commerce.Entity.Utente;
import it.BeGear.E_commerce.Exception.*;
import it.BeGear.E_commerce.Repository.ProdottoRepo;
import it.BeGear.E_commerce.Repository.UtenteRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProdottoService {

    @Autowired
    private ProdottoRepo prodottoRepo;
    @Autowired
    private UtenteRepo utenteRepo;
    @Autowired
    private UtenteService utenteService;


    public ProdottoDTO creaProdotto(ProdottoDTO prodottoDTO) {
        Prodotto prodotto = new Prodotto();
        ProdottoDtoMapper.DTOToProdotto(prodottoDTO, prodotto);

        if (prodotto.getSconto() < 0 || prodotto.getSconto() > 100) {
            throw new ScontoNonValidoException("Sconto non valido. Deve essere compreso tra 1 e 100");
        }

        Prodotto savedProdotto = prodottoRepo.save(prodotto);
        return ProdottoDtoMapper.prodottoToDTO(savedProdotto, new ProdottoDTO());
    }


    //Get di tutti i prodotti
    public List<ProdottoDTO> getAllProdotti() {
        List<Prodotto> prodotti = prodottoRepo.findAll();
        List<ProdottoDTO> prodottoDTO = new ArrayList<>();
        for (Prodotto prodotto : prodotti) {
            prodottoDTO.add(ProdottoDtoMapper.prodottoToDTO(prodotto, new ProdottoDTO()));
        }
        return prodottoDTO;
    }


    public ProdottoDTO updateProdotto(int id, ProdottoDTO prodottoDTO) {
        Prodotto prodotto = prodottoRepo.findById(id).orElseThrow(() ->
                new ProdottoAssenteException("Prodotto non trovato con id: " + id)
        );

        // Modifico i campi descrizione, prezzo e quantità solo se non sono null o 0
        if (prodottoDTO.getDescrizione() != null) prodotto.setDescrizione(prodottoDTO.getDescrizione());
        if (prodottoDTO.getPrezzo() != 0) prodotto.setPrezzo(prodottoDTO.getPrezzo());
        if (prodottoDTO.getQuantita() >= 0) prodotto.setQuantita(prodottoDTO.getQuantita());
        Prodotto updatedProdotto = prodottoRepo.save(prodotto);
        return ProdottoDtoMapper.prodottoToDTO(updatedProdotto, new ProdottoDTO());
    }

    public boolean deleteProdotto(int id) {
        if (!prodottoRepo.existsById(id)) {
            throw new ProdottoAssenteException("Prodotto con id: " + id + " non trovato");
        }
        prodottoRepo.deleteById(id);
        return true;
    }


    @Transactional
    public ProdottoDTO acquistaProdotto(int quantitaRichiesta, int id, int utenteId) {
        Prodotto prodotto = prodottoRepo.findById(id)
                .orElseThrow(() -> new ProdottoAssenteException("Prodotto non trovato con id: " + id));

        Utente utente = utenteRepo.findById(utenteId)
                .orElseThrow(() -> new UtenteAssenteException("Utente non trovato con id: " + utenteId));

        if (prodotto.getQuantita() < quantitaRichiesta) {
            throw new QuantitaNonDisponibileException(
                    "Quantità richiesta (" + quantitaRichiesta + ") non disponibile. Disponibilità attuale: " + prodotto.getQuantita());
        }

        double prezzoTotale = prodotto.getPrezzo() * quantitaRichiesta;

        if (utente.getSaldoWallett() < prezzoTotale) {
            throw new SaldoInsufficienteException("Saldo insufficiente per acquistare il prodotto. Richiesto: "
                    + prezzoTotale + ", Disponibile: " + utente.getSaldoWallett());
        }

        // Sottrai il saldo e aggiorna il wallet dell'utente
        utente.setSaldoWallett(utente.getSaldoWallett() - prezzoTotale);
        utenteRepo.save(utente);

        // Aggiorna la quantità del prodotto
        prodotto.setQuantita(prodotto.getQuantita() - quantitaRichiesta);
        prodotto.setQuantitaVenduta(prodotto.getQuantitaVenduta() + quantitaRichiesta);

        Prodotto updatedProdotto = prodottoRepo.save(prodotto);

        return ProdottoDtoMapper.prodottoToDTO(updatedProdotto, new ProdottoDTO());
    }


    public List<ProdottoDTO> getProdottiFiltratiPerSaldo(Principal principal){
        String username= principal.getName();
        Utente utente= utenteRepo.findByUsername(username).orElseThrow(() -> new UtenteAssenteException("Utente con username" + username + "non trovato"));
        double saldoWallett = utente.getSaldoWallett();
        // Filtro i prodotti con prezzo minore o uguale al saldo del wallett
        List<Prodotto> prodottiFiltrati= prodottoRepo.findProdottiByPrezzoMax(saldoWallett);

        List<ProdottoDTO> prodottiDTO= new ArrayList<>();
        for(Prodotto prodotto : prodottiFiltrati){
            prodottiDTO.add(ProdottoDtoMapper.prodottoToDTO(prodotto, new ProdottoDTO()));
        }
        return prodottiDTO;
    }

    public List<ProdottoDTO> getProdottiPiuVenduti(FasciaDiPrezzo fasciaDiPrezzo, int limit) {
        List<Prodotto> prodotti = prodottoRepo.findProdottiPiuVendutiPerFascia(
                fasciaDiPrezzo.getMin(),
                fasciaDiPrezzo.getMax()
        );
        List<Prodotto> prodottiLimitati = prodotti.subList(0, Math.min(limit, prodotti.size()));

        List<ProdottoDTO> prodottiDTO = new ArrayList<>();
        for (Prodotto prodotto : prodottiLimitati) {
            prodottiDTO.add(ProdottoDtoMapper.prodottoToDTO(prodotto, new ProdottoDTO()));
        }
        return prodottiDTO;
    }


    public Map<FasciaDiPrezzo, List<ProdottoDTO>> getProdottiPiuVendutiPerTutteLeFasce(int limitPerFascia){
        Map<FasciaDiPrezzo, List<ProdottoDTO>> risultati = new HashMap<>();
        for (FasciaDiPrezzo fascia: FasciaDiPrezzo.values()){
            risultati.put(fascia, getProdottiPiuVenduti(fascia, limitPerFascia));
        }
        return risultati;
    }

}