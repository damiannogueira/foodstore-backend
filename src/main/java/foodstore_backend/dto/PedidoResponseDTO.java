package foodstore_backend.dto;

import foodstore_backend.model.enums.EstadoPedido;
import foodstore_backend.model.enums.FormaPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// DTO de respuesta para devolver un pedido con sus detalles
public class PedidoResponseDTO {

    private Long id;
    private Long usuarioId;
    private String nombreCliente;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    private FormaPago formaPago;
    private BigDecimal total;
    private String telefono;
    private String direccionEntrega;
    private String notas;
    private List<DetallePedidoResponseDTO> detalles;

    public PedidoResponseDTO() {
    }

    public PedidoResponseDTO(Long id, Long usuarioId, String nombreCliente, LocalDateTime fecha,
                             EstadoPedido estado, FormaPago formaPago, BigDecimal total,
                             String telefono, String direccionEntrega, String notas,
                             List<DetallePedidoResponseDTO> detalles) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombreCliente = nombreCliente;
        this.fecha = fecha;
        this.estado = estado;
        this.formaPago = formaPago;
        this.total = total;
        this.telefono = telefono;
        this.direccionEntrega = direccionEntrega;
        this.notas = notas;
        this.detalles = detalles;
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public String getNotas() {
        return notas;
    }

    public List<DetallePedidoResponseDTO> getDetalles() {
        return detalles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public void setDetalles(List<DetallePedidoResponseDTO> detalles) {
        this.detalles = detalles;
    }
}