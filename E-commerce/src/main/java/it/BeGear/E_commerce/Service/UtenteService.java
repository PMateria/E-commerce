package it.BeGear.E_commerce.Service;

import it.BeGear.E_commerce.Dto.UtenteDTO;
import it.BeGear.E_commerce.Dto.UtenteDtoMapper;
import it.BeGear.E_commerce.Entity.Commento;
import it.BeGear.E_commerce.Entity.Utente;
import it.BeGear.E_commerce.Exception.UtenteAssenteException;
import it.BeGear.E_commerce.Exception.UtenteDoppioException;
import it.BeGear.E_commerce.Repository.CommentoRepo;
import it.BeGear.E_commerce.Repository.UtenteRepo;

import java.awt.print.Pageable;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UtenteService {

    @Autowired
    private UtenteRepo utenteRepository;
    @Autowired
    private CommentoRepo commentoRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public UtenteDTO registraUtente(UtenteDTO utenteDTO) {
        if (utenteRepository.existsByEmail(utenteDTO.getEmail())) {
            throw new UtenteDoppioException("Email già registrata");
        }
        Utente utente = new Utente();

        UtenteDtoMapper.DTOToUtente(utenteDTO, utente);
        Utente savedUtente = utenteRepository.save(utente);
        return UtenteDtoMapper.utenteDto(savedUtente, new UtenteDTO());
    }

    public boolean authenticateUser(String username, String rawPassword) {
        // Trova l'utente usando l'username
        return utenteRepository.findByUsername(username)
                .map(utente -> passwordEncoder.matches(rawPassword, utente.getPassword()))
                .orElse(false);
    }

    // Get dell'utente in base all'id
    public UtenteDTO getUtenteById(int id) {
        Utente utente = utenteRepository.findById(id).orElseThrow(() ->
                new UtenteAssenteException("Utente con id " + id + " non trovato")
        );
        return UtenteDtoMapper.utenteDto(utente, new UtenteDTO());
    }

    //Get di tutti gli utenti
    public List<UtenteDTO> getAllUtenti() {
        List<Utente> utenti = utenteRepository.findAll();
        List<UtenteDTO> utentiDTO = new ArrayList<>();
        for (Utente utente : utenti) {
            utentiDTO.add(UtenteDtoMapper.utenteDto(utente, new UtenteDTO()));
        }
        return utentiDTO;
    }

    //Modifica utente
    public UtenteDTO updateUtente(int id, UtenteDTO utenteDTO) {
        Utente utente = utenteRepository.findById(id).orElse(null);
        if (utente == null) {
            throw new UtenteAssenteException("Utente con id " + id + " non trovato");
        }
        utente.setNome(utenteDTO.getNome());
        utente.setCognome(utente.getCognome());

        // Verifico se l'email non è già associata ad un altro utente
        if (!utente.getEmail().equals(utenteDTO.getEmail()) && utenteRepository.existsByEmail(utenteDTO.getEmail())) {
            throw new UtenteDoppioException("Email già registrata per un altro utente");
        }

        utente.setEmail(utenteDTO.getEmail());

        if (utenteDTO.getPassword() != null) {
            utente.setPassword(utenteDTO.getPassword());
        }

        Utente updatedUtente = utenteRepository.save(utente);
        return UtenteDtoMapper.utenteDto(updatedUtente, new UtenteDTO());
    }

    //cancella utente per id
    public void deleteUtente(int id) {
        if (!utenteRepository.existsById(id)) {
            throw new UtenteAssenteException("Utente non trovato");
        }
        utenteRepository.deleteById(id);
    }

    public void aggiungiSaldoWallet(double somma, String username) {
        // Trova l'utente usando il parametro username
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UtenteAssenteException("Utente con username " + username + " non trovato"));

        // Aggiungi il saldo al wallet dell'utente
        utente.setSaldoWallett(utente.getSaldoWallett() + somma);
        utenteRepository.save(utente);
    }


    public boolean sottraiSaldo(double somma, String username){
        // Trova l'utente usando l'username
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UtenteAssenteException("Utente con username " + username + " non trovato"));

        // Verifica se l'utente ha saldo sufficiente
        if (utente.getSaldoWallett() >= somma) {

            //Sottrai saldo da SaldoWallett dell'utente
            utente.setSaldoWallett(utente.getSaldoWallett() - somma);
            utenteRepository.save(utente);
            return true;
        } else {
            return false;
        }
    }

    public double getSaldoWallett(String username) {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UtenteAssenteException("Utente con username " + username + " non trovato"));
        return utente.getSaldoWallett();
    }


}