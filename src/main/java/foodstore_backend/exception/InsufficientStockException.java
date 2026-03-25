package foodstore_backend.exception;

// Excepción para cuando no hay stock suficiente de un producto
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String mensaje) {
        super(mensaje);
    }
}
