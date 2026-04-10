package foodstore_backend.dto;

import java.math.BigDecimal;

// DTO de detalle de pedido
public record DetallePedidoResponseDTO(
        Long id,
        Integer cantidad,
        BigDecimal subtotal,
        ProductoPedidoResponseDTO producto
) {
}
