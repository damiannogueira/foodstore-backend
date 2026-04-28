package foodstore_backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

// DTO para crear un producto nuevo
public record ProductoCreateDTO(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(min = 2, max = 500, message = "La descripción debe tener entre 2 y 500 caracteres")
        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
        BigDecimal precio,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,

        @NotBlank(message = "La imagen es obligatoria")
        @Size(max = 1000, message = "La URL de imagen no puede exceder 1000 caracteres")
        String imagen,

        @NotNull(message = "El estado de disponibilidad es obligatorio")
        Boolean disponible,

        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId

) {
}