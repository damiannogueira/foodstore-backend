package foodstore_backend.dto;

import foodstore_backend.model.enums.FormaPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

// DTO para crear pedidos
public class PedidoCreateDTO {

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "La forma de pago es obligatoria")
    private FormaPago formaPago;

    @Valid
    @NotEmpty(message = "El pedido debe tener al menos un detalle")
    private List<PedidoDetalleCreateDTO> detalles;

    public PedidoCreateDTO() {
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public List<PedidoDetalleCreateDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<PedidoDetalleCreateDTO> detalles) {
        this.detalles = detalles;
    }
}

