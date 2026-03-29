package foodstore_backend.repository;

import foodstore_backend.model.Categoria;

import java.util.List;
import java.util.Optional;

// Repositorio que permite acceder a la base de datos para la entidad Categoria
public interface CategoriaRepository extends BaseRepository<Categoria, Long> {

    Optional<Categoria> findByNombreIgnoreCaseAndEliminadoFalse(String nombre);

    List<Categoria> findByEliminadoFalse();
}
