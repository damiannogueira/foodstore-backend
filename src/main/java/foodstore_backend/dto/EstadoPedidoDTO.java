package foodstore_backend.dto;

import foodstore_backend.model.enums.EstadoPedido;
import jakarta.validation.constraints.NotNull;

// DTO para actualizar solo estado
public record EstadoPedidoDTO(
        @NotNull(message = "El estado es obligatorio")
        EstadoPedido estado
) {
}