package it.BeGear.E_commerce.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "prodotti")
public class Prodotto extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String descrizione;
    private int prezzo;
    private int quantita;
    private int quantitaVenduta;
    private int codiceProdotto;
    private String immagineUrl;

    @Max(value = 100, message = "Lo sconto non può superare il 100")
    private Integer sconto;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @OneToMany(mappedBy = "prodotto", fetch = FetchType.LAZY)
    private List<Commento> commenti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

}
