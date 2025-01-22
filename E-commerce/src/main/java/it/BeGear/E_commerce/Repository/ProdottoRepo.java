package it.BeGear.E_commerce.Repository;

import it.BeGear.E_commerce.Entity.Prodotto;
import it.BeGear.E_commerce.Entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "prodotti")
public interface ProdottoRepo extends JpaRepository<Prodotto, Integer> {

    List<Prodotto> findByUtente(Utente utente);

    @Query("SELECT p FROM Prodotto p WHERE p.prezzo <= :prezzoMax")
    List<Prodotto> findProdottiByPrezzoMax(@Param("prezzoMax") double prezzoMax);

    @Query("SELECT p FROM Prodotto p WHERE p.prezzo BETWEEN :minPrezzo AND :maxPrezzo ORDER BY p.quantitaVenduta DESC")
    List<Prodotto> findProdottiPiuVendutiPerFascia(double minPrezzo, double maxPrezzo);


}
