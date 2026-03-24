package foodstore_backend.exception;

// Excepción para cuando un recurso no se encuentra
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}