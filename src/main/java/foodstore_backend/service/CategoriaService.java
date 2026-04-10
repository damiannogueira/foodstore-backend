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
        return categoriaRepository.findAllByEliminadoFalse()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public CategoriaResponseDTO obtenerPorId(Long id) {
        return toResponseDTO(buscarPorId(id));
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada con id: " + id
                ));
    }

    // Guarda una categoría validando nombre duplicado
    public CategoriaResponseDTO guardarCategoria(CategoriaCreateDTO dto) {

        categoriaRepository.findByNombreIgnoreCaseAndEliminadoFalse(dto.nombre())
                .ifPresent(c -> {
                    throw new DuplicateResourceException(
                            "La categoría ya existe con nombre: " + dto.nombre()
                    );
                });

        validarImagen(dto.imagen());

        Categoria categoria = new Categoria();
        categoria.setNombre(dto.nombre().trim());
        categoria.setDescripcion(dto.descripcion().trim());
        categoria.setImagen(dto.imagen().trim());
        categoria.setEliminado(false);

        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return toResponseDTO(categoriaGuardada);
    }

    // Actualiza solo los campos enviados
    public CategoriaResponseDTO actualizarCategoria(Long id, CategoriaEditDTO dto) {
        Categoria categoria = buscarPorId(id);

        if (dto.nombre() != null) {
            String nuevoNombre = dto.nombre().trim();

            if (!nuevoNombre.equalsIgnoreCase(categoria.getNombre())) {
                categoriaRepository.findByNombreIgnoreCaseAndEliminadoFalse(nuevoNombre)
                        .ifPresent(c -> {
                            throw new DuplicateResourceException(
                                    "La categoría ya existe con nombre: " + nuevoNombre
                            );
                        });
            }

            categoria.setNombre(nuevoNombre);
        }

        if (dto.descripcion() != null) {
            categoria.setDescripcion(dto.descripcion().trim());
        }

        if (dto.imagen() != null) {
            validarImagen(dto.imagen());
            categoria.setImagen(dto.imagen().trim());
        }

        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return toResponseDTO(categoriaGuardada);
    }

    // Realiza baja lógica
    public void eliminarCategoria(Long id) {
        Categoria categoria = buscarPorId(id);
        categoria.setEliminado(true);
        categoriaRepository.save(categoria);
    }

    private void validarImagen(String imagen) {
        String valor = imagen == null ? "" : imagen.trim().toLowerCase();

        boolean esValida = valor.startsWith("http://") || valor.startsWith("https://");

        if (!esValida) {
            throw new IllegalArgumentException("La imagen debe ser una URL válida");
        }
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getImagen()
        );
    }
}