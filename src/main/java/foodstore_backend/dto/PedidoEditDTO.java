package foodstore_backend.dto;

import foodstore_backend.model.enums.EstadoPedido;
import foodstore_backend.model.enums.FormaPago;
import jakarta.validation.constraints.Size;

// DTO para editar datos permitidos de un pedido
public class PedidoEditDTO {

    private EstadoPedido estado;
    private FormaPago formaPago;

    @Size(max = 50, message = "El teléfono no puede exceder 50 caracteres")
    private String telefono;

    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    private String direccionEntrega;

    @Size(max = 500, message = "Las notas no pueden exceder 500 caracteres")
    private String notas;

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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}