package foodstore_backend.service;

import foodstore_backend.exception.DuplicateResourceException;
import foodstore_backend.exception.ResourceNotFoundException;
import foodstore_backend.model.Categoria;
import foodstore_backend.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Servicio que contiene la lógica de negocio para manejar categorías
@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Devuelve todas las categorías
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    // Busca una categoría por ID o lanza excepción si no existe
    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
    }

    // Guarda una categoría validando nombre duplicado
    public Categoria guardarCategoria(Categoria categoria) {
        categoriaRepository.findByNombre(categoria.getNombre())
                .ifPresent(c -> {
                    throw new DuplicateResourceException("La categoría ya existe");
                });

        return categoriaRepository.save(categoria);
    }

    // Elimina una categoría verificando que exista
    public void eliminarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));

        categoriaRepository.delete(categoria);
    }

    // Actualiza una categoría existente
    public Categoria actualizarCategoria(Long id, Categoria categoriaActualizada) {

        Categoria categoriaExistente = categoriaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));

        categoriaRepository.findByNombre(categoriaActualizada.getNombre())
            .ifPresent(c -> {
                if (!c.getId().equals(id)) {
                    throw new DuplicateResourceException("La categoría ya existe");
                }
            });

        categoriaExistente.setNombre(categoriaActualizada.getNombre());
        categoriaExistente.setDescripcion(categoriaActualizada.getDescripcion());

        return categoriaRepository.save(categoriaExistente);
    }
}
