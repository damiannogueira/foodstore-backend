package foodstore_backend.dto;

import java.time.LocalDateTime;

// DTO estándar para respuestas de error
public record ApiError(
        String error,
        String message,
        int status,
        LocalDateTime timestamp
) {

    public ApiError(String error, String message, int status) {
        this(error, message, status, LocalDateTime.now());
    }
}
