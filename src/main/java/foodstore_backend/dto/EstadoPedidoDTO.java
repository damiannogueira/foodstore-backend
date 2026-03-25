package foodstore_backend.dto;

import foodstore_backend.model.enums.EstadoPedido;
import jakarta.validation.constraints.NotNull;

// DTO para actualizar el estado de un pedido
public class EstadoPedidoDTO {

    @NotNull(message = "El estado es obligatorio")
    private EstadoPedido estado;

    public EstadoPedidoDTO() {
    }

    public EstadoPedidoDTO(EstadoPedido estado) {
        this.estado = estado;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }
}