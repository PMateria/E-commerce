package it.BeGear.E_commerce.Configuration;

import it.BeGear.E_commerce.Entity.Utente;
import it.BeGear.E_commerce.Repository.UtenteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EcomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UtenteRepo utenteRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente utente = utenteRepo.findByUsername(username).orElseThrow(() -> new
                UsernameNotFoundException("L'utente con username: " + username + "non esiste"));
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(utente.getRuolo()));
        return new User(utente.getUsername(), utente.getPassword(), authorities);
    }
}