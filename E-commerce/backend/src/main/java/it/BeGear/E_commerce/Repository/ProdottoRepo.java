package it.BeGear.E_commerce.Repository;

import it.BeGear.E_commerce.Entity.Prodotto;
import it.BeGear.E_commerce.Entity.Utente;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Transactional
    @Modifying
    @Query(value = "UPDATE magazzino.fornitori SET prodotto_fornitore = CONCAT(prodotto_fornitore, ',', ?1) " +
            "WHERE codice_fornitore = ?2 AND NOT FIND_IN_SET(?1, prodotto_fornitore)", nativeQuery = true)
    public void assegnazioneFornitore(int codiceProdotto, int codiceFornitore);

    @Query("SELECT p FROM Prodotto p WHERE p.codiceProdotto IN :codiceProdotto")
    List<Prodotto> findByCodiceProdotto(@Param("codiceProdotto") List<Integer> codiceProdotto);

}
