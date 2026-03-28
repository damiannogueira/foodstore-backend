package foodstore_backend.repository;

import foodstore_backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repositorio para acceder a los datos de productos
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Busca producto por nombre
    Optional<Producto> findByNombre(String nombre);

    // Devuelve productos no eliminados
    List<Producto> findByEliminadoFalse();

    // Devuelve productos no eliminados por categoría
    List<Producto> findByCategoriaIdAndEliminadoFalse(Long categoriaId);
}