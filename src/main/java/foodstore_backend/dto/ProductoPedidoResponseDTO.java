package foodstore_backend.dto;

import java.math.BigDecimal;

// DTO de producto dentro del pedido
public record ProductoPedidoResponseDTO(
        Long id,
        String nombre,
        BigDecimal precio,
        Integer stock,
        String imagen,
        Boolean disponible
) {
}
