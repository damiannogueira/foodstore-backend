package foodstore_backend.repository;

import foodstore_backend.model.Producto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repositorio para acceder a los datos de productos
@Repository
public interface ProductoRepository extends BaseRepository<Producto, Long> {

    Optional<Producto> findByNombreIgnoreCaseAndEliminadoFalse(String nombre);

    List<Producto> findByEliminadoFalse();

    List<Producto> findByCategoriaIdAndEliminadoFalse(Long categoriaId);

    List<Producto> findByDisponibleTrueAndEliminadoFalse();
}