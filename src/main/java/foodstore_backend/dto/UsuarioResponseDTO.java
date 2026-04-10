package foodstore_backend.dto;

import foodstore_backend.model.enums.Rol;

// DTO para responder datos de usuario sin password
public record UsuarioResponseDTO(
        Long id,
        String nombre,
        String apellido,
        String email,
        String celular,
        Rol rol
) {
}
