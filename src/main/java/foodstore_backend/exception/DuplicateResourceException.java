package foodstore_backend.exception;

// Excepción para cuando se intenta crear un recurso duplicado
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String mensaje) {
        super(mensaje);
    }
}
