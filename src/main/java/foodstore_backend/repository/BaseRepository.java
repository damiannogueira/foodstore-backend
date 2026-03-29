package foodstore_backend.repository;

import foodstore_backend.exception.ResourceNotFoundException;
import foodstore_backend.model.Base;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

// Repositorio base para centralizar soft delete y búsquedas comunes
@NoRepositoryBean
public interface BaseRepository<T extends Base, ID> extends JpaRepository<T, ID> {

    // Devuelve solo registros no eliminados
    List<T> findAllByEliminadoFalse();

    // Busca una entidad por id solo si no está eliminada
    Optional<T> findByIdAndEliminadoFalse(ID id);

    // Sobrescribe findAll para filtrar eliminados
    @Override
    default List<T> findAll() {
        return findAllByEliminadoFalse();
    }

    // Busca por id o lanza excepción si no existe o está eliminada
    default T findByIdOrThrow(ID id, String entityName) {
        return findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName + " no encontrado con id: " + id));
    }

    // Soft delete genérico
    @Override
    @Transactional
    default void deleteById(ID id) {
        T entity = findByIdOrThrow(id, "Recurso");
        entity.setEliminado(true);
        save(entity);
    }
}