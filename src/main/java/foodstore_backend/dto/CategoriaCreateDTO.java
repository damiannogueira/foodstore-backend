package foodstore_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// DTO para crear una categoría nueva
public record CategoriaCreateDTO(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        String descripcion,

        @NotBlank(message = "La imagen es obligatoria")
        @Size(max = 1000, message = "La URL de imagen no puede exceder 1000 caracteres")
        String imagen

) {
}
