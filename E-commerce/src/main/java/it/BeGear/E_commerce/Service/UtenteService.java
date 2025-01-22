package it.BeGear.E_commerce.Service;
import it.BeGear.E_commerce.Dto.UtenteDTO;
import it.BeGear.E_commerce.Dto.UtenteDtoMapper;
import it.BeGear.E_commerce.Entity.Utente;
import it.BeGear.E_commerce.Exception.UtenteAssenteException;
import it.BeGear.E_commerce.Repository.CommentoRepo;
import it.BeGear.E_commerce.Repository.UtenteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class UtenteService {

    @Autowired
    private UtenteRepo utenteRepository;
    @Autowired
    private CommentoRepo commentoRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;


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
        Utente utente = utenteRepository.findById(id).orElse(null)  ;

        if (utente == null) {
            throw new UtenteAssenteException("Utente con id " + id + " non trovato");
        }else {
            utente.setNome(utenteDTO.getNome());
            utente.setCognome(utenteDTO.getCognome());
            if (!Objects.equals(utente.getEmail(), utenteDTO.getEmail())) {
                utente.setEmail(utente.getEmail());
            }


            if (utenteDTO.getPassword() != null) {
                utente.setPassword(utenteDTO.getPassword());
            }
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