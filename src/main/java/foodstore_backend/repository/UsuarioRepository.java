package foodstore_backend.repository;

import foodstore_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Repositorio que permite acceder a la base de datos para la entidad Usuario
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByEliminadoFalse();

}
