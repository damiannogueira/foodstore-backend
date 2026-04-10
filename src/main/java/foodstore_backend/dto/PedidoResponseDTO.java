package foodstore_backend.dto;

import foodstore_backend.model.enums.EstadoPedido;
import foodstore_backend.model.enums.FormaPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// DTO de respuesta de pedido
public record PedidoResponseDTO(
        Long id,
        Long usuarioId,
        String nombreCliente,
        LocalDateTime fecha,
        EstadoPedido estado,
        FormaPago formaPago,
        BigDecimal total,
        String telefono,
        String direccionEntrega,
        String notas,
        List<DetallePedidoResponseDTO> detalles
) {
}