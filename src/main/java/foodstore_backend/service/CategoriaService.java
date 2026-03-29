package foodstore_backend.service;

import foodstore_backend.dto.CategoriaCreateDTO;
import foodstore_backend.dto.CategoriaEditDTO;
import foodstore_backend.dto.CategoriaResponseDTO;
import foodstore_backend.exception.DuplicateResourceException;
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
        return categoriaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public CategoriaResponseDTO obtenerPorId(Long id) {
        return toResponseDTO(buscarPorId(id));
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findByIdOrThrow(id, "Categoría");
    }

    // Guarda una categoría validando nombre duplicado
    public CategoriaResponseDTO guardarCategoria(CategoriaCreateDTO dto) {

        categoriaRepository.findByNombreIgnoreCaseAndEliminadoFalse(dto.getNombre())
                .ifPresent(c -> {
                    throw new DuplicateResourceException(
                            "La categoría ya existe con nombre: " + dto.getNombre()
                    );
                });

        validarImagen(dto.getImagen());

        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre().trim());
        categoria.setDescripcion(dto.getDescripcion().trim());
        categoria.setImagen(dto.getImagen().trim());
        categoria.setEliminado(false);

        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return toResponseDTO(categoriaGuardada);
    }

    // Actualiza solo los campos enviados
    public CategoriaResponseDTO actualizarCategoria(Long id, CategoriaEditDTO dto) {
        Categoria categoria = buscarPorId(id);

        if (dto.getNombre() != null) {
            String nuevoNombre = dto.getNombre().trim();

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

        if (dto.getDescripcion() != null) {
            categoria.setDescripcion(dto.getDescripcion().trim());
        }

        if (dto.getImagen() != null) {
            validarImagen(dto.getImagen());
            categoria.setImagen(dto.getImagen().trim());
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