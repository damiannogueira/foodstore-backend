package foodstore_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

// DTO para editar usuario con campos opcionales
public record UsuarioEditDTO(

        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,

        @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
        String apellido,

        @Email(message = "El email debe tener un formato válido")
        @Size(max = 150, message = "El email no puede superar los 150 caracteres")
        String email,

        @Size(max = 20, message = "El celular no puede superar los 20 caracteres")
        String celular,

        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password

) {
}
