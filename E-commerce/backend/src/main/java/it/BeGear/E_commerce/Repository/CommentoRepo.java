package it.BeGear.E_commerce.Repository;

import it.BeGear.E_commerce.Entity.Commento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentoRepo extends JpaRepository<Commento, Integer> {
    List<Commento> findByProdottoId(int prodottoId);
}
