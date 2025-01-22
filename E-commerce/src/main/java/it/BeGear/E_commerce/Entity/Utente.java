package it.BeGear.E_commerce.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Utenti")
public class Utente extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private String cognome;
    private String email;
    private String username;
    private String password;
    private String telefono;
    private String genere;
    private String ruolo;
    private double saldoWallett;


    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    private List<Prodotto> prodotti;

    @OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
    private List<Commento> commenti;

}