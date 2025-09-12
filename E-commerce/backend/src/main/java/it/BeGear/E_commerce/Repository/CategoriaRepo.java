package it.BeGear.E_commerce.Repository;

import it.BeGear.E_commerce.Entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepo extends JpaRepository<Categoria, Long> {


    boolean existsByNome(String nome);

    Optional<Categoria> findByNome(String nome);

}
