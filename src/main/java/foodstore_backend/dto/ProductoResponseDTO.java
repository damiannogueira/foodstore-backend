package foodstore_backend.dto;

import java.math.BigDecimal;

// DTO de respuesta para devolver datos de un producto
public record ProductoResponseDTO(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        String imagen,
        Boolean disponible,
        CategoriaResponseDTO categoria
) {
}
