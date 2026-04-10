package foodstore_backend.dto;

// DTO de respuesta para devolver datos de una categoría
public record CategoriaResponseDTO(
        Long id,
        String nombre,
        String descripcion,
        String imagen
) {
}