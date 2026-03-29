package foodstore_backend.dto;

import java.time.LocalDateTime;

// DTO estándar para respuestas de error
public class ApiError {

    private String error;
    private String message;
    private int status;
    private LocalDateTime timestamp;

    public ApiError(String error, String message, int status) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
