package foodstore_backend.dto;

import foodstore_backend.model.enums.Rol;

// DTO de respuesta para login o registro exitoso
public record LoginResponseDTO(
        Long id,
        String nombre,
        String apellido,
        String email,
        Rol rol,
        String mensaje
) {
}
