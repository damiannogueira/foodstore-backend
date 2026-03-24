package foodstore_backend.repository;

import foodstore_backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repositorio que permite acceder a la base de datos para la entidad Producto
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByNombre(String nombre);
}