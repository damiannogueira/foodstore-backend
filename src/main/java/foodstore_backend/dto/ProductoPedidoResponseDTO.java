package foodstore_backend.dto;

import java.math.BigDecimal;

// DTO resumido de producto dentro del detalle de pedido
public class ProductoPedidoResponseDTO {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private String imagen;
    private Boolean disponible;

    public ProductoPedidoResponseDTO() {
    }

    public ProductoPedidoResponseDTO(Long id, String nombre, BigDecimal precio,
                                     Integer stock, String imagen, Boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.imagen = imagen;
        this.disponible = disponible;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public Integer getStock() {
        return stock;
    }

    public String getImagen() {
        return imagen;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}
