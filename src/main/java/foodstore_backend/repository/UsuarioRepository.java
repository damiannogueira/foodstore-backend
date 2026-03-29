package foodstore_backend.repository;

import foodstore_backend.model.Usuario;

import java.util.List;
import java.util.Optional;

// Repositorio que permite acceder a la base de datos para la entidad Usuario
public interface UsuarioRepository extends BaseRepository<Usuario, Long> {

    Optional<Usuario> findByEmailAndEliminadoFalse(String email);

    List<Usuario> findByEliminadoFalse();
}
