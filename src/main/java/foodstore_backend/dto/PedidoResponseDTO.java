package foodstore_backend.dto;

import foodstore_backend.model.enums.EstadoPedido;
import foodstore_backend.model.enums.FormaPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// DTO de respuesta para pedidos
public class PedidoResponseDTO {

    private Long id;
    private Long usuarioId;
    private String nombreUsuario;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    private FormaPago formaPago;
    private BigDecimal total;
    private List<DetallePedidoResponseDTO> detalles;

    public PedidoResponseDTO() {
    }

    public PedidoResponseDTO(Long id, Long usuarioId, String nombreUsuario, LocalDateTime fecha,
                             EstadoPedido estado, FormaPago formaPago, BigDecimal total,
                             List<DetallePedidoResponseDTO> detalles) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombreUsuario = nombreUsuario;
        this.fecha = fecha;
        this.estado = estado;
        this.formaPago = formaPago;
        this.total = total;
        this.detalles = detalles;
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
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

    public List<DetallePedidoResponseDTO> getDetalles() {
        return detalles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
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

    public void setDetalles(List<DetallePedidoResponseDTO> detalles) {
        this.detalles = detalles;
    }
}