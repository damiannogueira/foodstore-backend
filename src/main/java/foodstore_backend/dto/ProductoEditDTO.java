package foodstore_backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

// DTO para editar producto (campos opcionales)
public record ProductoEditDTO(

        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        String descripcion,

        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
        BigDecimal precio,

        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,

        @Size(max = 1000, message = "La URL de imagen no puede exceder 1000 caracteres")
        String imagen,

        Boolean disponible,

        Long categoriaId

) {
}