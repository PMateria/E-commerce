package it.BeGear.E_commerce.Repository;

import it.BeGear.E_commerce.Entity.Carrello;
import it.BeGear.E_commerce.Entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrelloRepo extends JpaRepository<Carrello, Integer> {
    Optional<Carrello> findByUtente(Utente utente);

}
