package foodstore_backend.dto;

import foodstore_backend.model.enums.EstadoPedido;
import foodstore_backend.model.enums.FormaPago;

// DTO para editar pedido (solo estado y forma de pago)
public record PedidoEditDTO(
        EstadoPedido estado,
        FormaPago formaPago
) {
}