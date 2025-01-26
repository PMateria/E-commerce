package it.BeGear.E_commerce.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Carrelli")
public class Carrello {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "carrello_id")
    private List<CarrelloItem> items = new ArrayList<>();

    private double totale;



}
