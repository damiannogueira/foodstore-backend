package foodstore_backend.service;

import foodstore_backend.dto.CategoriaCreateDTO;
import foodstore_backend.dto.CategoriaEditDTO;
import foodstore_backend.dto.CategoriaResponseDTO;
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

    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaRepository.findByEliminadoFalse()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public CategoriaResponseDTO obtenerPorId(Long id) {
        return toResponseDTO(buscarPorId(id));
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
    }

    public CategoriaResponseDTO guardarCategoria(CategoriaCreateDTO categoriaCreateDTO) {

        categoriaRepository.findByNombre(categoriaCreateDTO.getNombre())
                .ifPresent(c -> {
                    throw new DuplicateResourceException(
                            "La categoría ya existe con nombre: " + categoriaCreateDTO.getNombre()
                    );
                });

        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaCreateDTO.getNombre());
        categoria.setDescripcion(categoriaCreateDTO.getDescripcion());
        categoria.setEliminado(false);

        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return toResponseDTO(categoriaGuardada);
    }

    public CategoriaResponseDTO actualizarCategoria(Long id, CategoriaEditDTO categoriaEditDTO) {
        Categoria categoria = buscarPorId(id);

        String nuevoNombre = categoriaEditDTO.getNombre();
        if (nuevoNombre != null && !nuevoNombre.equalsIgnoreCase(categoria.getNombre())) {
            categoriaRepository.findByNombre(nuevoNombre)
                    .ifPresent(c -> {
                        throw new DuplicateResourceException(
                                "La categoría ya existe con nombre: " + nuevoNombre
                        );
                    });
            categoria.setNombre(nuevoNombre);
        }

        String nuevaDescripcion = categoriaEditDTO.getDescripcion();
        if (nuevaDescripcion != null) {
            categoria.setDescripcion(nuevaDescripcion);
        }

        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return toResponseDTO(categoriaGuardada);
    }

    public void eliminarCategoria(Long id) {
        Categoria categoria = buscarPorId(id);
        categoria.setEliminado(true);
        categoriaRepository.save(categoria);
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }
}