package foodstore_backend.dto;

import foodstore_backend.model.enums.FormaPago;

// DTO para editar pedido (por ahora simple)
public class PedidoEditDTO {

    private FormaPago formaPago;

    public PedidoEditDTO() {
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }
}