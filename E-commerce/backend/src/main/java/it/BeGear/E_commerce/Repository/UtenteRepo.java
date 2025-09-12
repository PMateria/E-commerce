package it.BeGear.E_commerce.Repository;

import it.BeGear.E_commerce.Entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path="utenti")
public interface UtenteRepo extends JpaRepository<Utente, Integer> {

    public boolean existsByEmail(String email);
    Optional <Utente> findByUsername(String username);

    boolean existsByUsername(String username);
}