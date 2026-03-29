package foodstore_backend.dto;

import java.math.BigDecimal;

// DTO de respuesta para una línea de detalle de pedido
public class DetallePedidoResponseDTO {

    private Long id;
    private Integer cantidad;
    private BigDecimal subtotal;
    private ProductoPedidoResponseDTO producto;

    public DetallePedidoResponseDTO() {
    }

    public DetallePedidoResponseDTO(Long id, Integer cantidad, BigDecimal subtotal,
                                    ProductoPedidoResponseDTO producto) {
        this.id = id;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.producto = producto;
    }

    public Long getId() {
        return id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public ProductoPedidoResponseDTO getProducto() {
        return producto;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public void setProducto(ProductoPedidoResponseDTO producto) {
        this.producto = producto;
    }
}
