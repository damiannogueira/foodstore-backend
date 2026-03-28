package foodstore_backend.repository;

import foodstore_backend.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Repositorio que permite acceder a la base de datos para la entidad Categoria
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNombre(String nombre);

    List<Categoria> findByEliminadoFalse();
}
