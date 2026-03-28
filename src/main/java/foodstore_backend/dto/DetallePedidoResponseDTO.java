package foodstore_backend.dto;

import java.math.BigDecimal;

// DTO de respuesta para cada detalle de pedido
public class DetallePedidoResponseDTO {

    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal subtotal;

    public DetallePedidoResponseDTO() {
    }

    public DetallePedidoResponseDTO(Long productoId, String nombreProducto, Integer cantidad, BigDecimal subtotal) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
