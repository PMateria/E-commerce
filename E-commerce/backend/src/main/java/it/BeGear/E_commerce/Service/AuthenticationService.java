package it.BeGear.E_commerce.Service;

import it.BeGear.E_commerce.Dto.ResponseDTO;
import it.BeGear.E_commerce.Dto.UtenteDTO;
import it.BeGear.E_commerce.Dto.UtenteDtoMapper;
import it.BeGear.E_commerce.Entity.Utente;
import it.BeGear.E_commerce.Exception.UtenteAssenteException;
import it.BeGear.E_commerce.Repository.UtenteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    UtenteRepo repository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;

    public ResponseDTO register(Utente utenteRequest) {
        if (repository.existsByEmail(utenteRequest.getEmail())) {
            throw new RuntimeException("Email già registrata. Utilizzare un indirizzo email diverso.");
        }

        if (repository.existsByUsername(utenteRequest.getUsername())) {
            throw new RuntimeException("Username già in uso. Scegliere un username diverso.");
        }
        Utente utente = utenteRequest.builder()
                .nome(utenteRequest.getNome())
                .cognome(utenteRequest.getCognome())
                .email(utenteRequest.getEmail())
                .username(utenteRequest.getUsername())
                .password(passwordEncoder.encode(utenteRequest.getPassword()))
                .telefono(utenteRequest.getTelefono())
                .genere(utenteRequest.getGenere())
                .ruolo(utenteRequest.getRuolo())
                .saldoWallett(utenteRequest.getSaldoWallett())
                .build();
        Utente savedUser = repository.save(utente);
        String jwtToken = jwtService.generateToken(utente);
        return new ResponseDTO(jwtToken, "200 - Registrazione riuscita");

    }

    public ResponseDTO authenticate(Utente utenteRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(utenteRequest.getUsername(), utenteRequest.getPassword()));
        Utente user = repository.findByUsername(utenteRequest.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return new ResponseDTO(jwtToken, "200 - Autenticazione riuscita");
    }


}
