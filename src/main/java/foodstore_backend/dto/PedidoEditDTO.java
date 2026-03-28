package foodstore_backend.dto;

import foodstore_backend.model.enums.EstadoPedido;
import foodstore_backend.model.enums.FormaPago;

// DTO para editar pedido
public class PedidoEditDTO {

    private EstadoPedido estado;
    private FormaPago formaPago;

    public PedidoEditDTO() {
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }
}