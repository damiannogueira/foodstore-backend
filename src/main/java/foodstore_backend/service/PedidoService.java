package foodstore_backend.service;

import foodstore_backend.dto.*;
import foodstore_backend.exception.InsufficientStockException;
import foodstore_backend.model.DetallePedido;
import foodstore_backend.model.Pedido;
import foodstore_backend.model.Producto;
import foodstore_backend.model.Usuario;
import foodstore_backend.model.enums.EstadoPedido;
import foodstore_backend.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Servicio que contiene la lógica de negocio para manejar pedidos
@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    public List<PedidoResponseDTO> listarPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<PedidoResponseDTO> listarPedidosPorUsuario(Long usuarioId) {
        usuarioService.buscarEntidadPorId(usuarioId);

        return pedidoRepository.findByUsuarioIdAndEliminadoFalse(usuarioId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<PedidoResponseDTO> listarPedidosPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstadoAndEliminadoFalse(estado)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findByIdOrThrow(id, "Pedido");
    }

    public PedidoResponseDTO obtenerPorId(Long id) {
        return toResponseDTO(buscarPorId(id));
    }

    @Transactional
    public PedidoResponseDTO guardarPedido(PedidoCreateDTO dto) {

        Usuario usuario = usuarioService.buscarEntidadPorId(dto.getUsuarioId());

        List<Producto> productosValidados = new ArrayList<>();
        List<PedidoDetalleCreateDTO> items = dto.getDetalles();

        for (PedidoDetalleCreateDTO item : items) {
            Producto producto = productoService.buscarPorId(item.getProductoId());

            if (!Boolean.TRUE.equals(producto.getDisponible())) {
                throw new IllegalArgumentException(
                        "El producto '" + producto.getNombre() + "' no está disponible para la venta"
                );
            }

            if (producto.getStock() < item.getCantidad()) {
                throw new InsufficientStockException(
                        "Stock insuficiente para el producto: " + producto.getNombre()
                );
            }

            productosValidados.add(producto);
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFormaPago(dto.getFormaPago());
        pedido.setTelefono(dto.getTelefono().trim());
        pedido.setDireccionEntrega(dto.getDireccionEntrega().trim());
        pedido.setNotas(dto.getNotas() != null ? dto.getNotas().trim() : null);
        pedido.setEliminado(false);

        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < items.size(); i++) {
            PedidoDetalleCreateDTO item = items.get(i);
            Producto producto = productosValidados.get(i);

            BigDecimal subtotal = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(item.getCantidad()));

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setSubtotal(subtotal);
            detalle.setEliminado(false);

            pedido.getDetalles().add(detalle);

            producto.setStock(producto.getStock() - item.getCantidad());

            total = total.add(subtotal);
        }

        pedido.setTotal(total);

        Pedido guardado = pedidoRepository.save(pedido);
        return toResponseDTO(guardado);
    }

    @Transactional
    public PedidoResponseDTO actualizarPedido(Long id, PedidoEditDTO dto) {
        Pedido pedido = buscarPorId(id);

        if (dto.getEstado() != null) {
            pedido.setEstado(dto.getEstado());
        }

        if (dto.getFormaPago() != null) {
            pedido.setFormaPago(dto.getFormaPago());
        }

        if (dto.getTelefono() != null) {
            pedido.setTelefono(dto.getTelefono().trim());
        }

        if (dto.getDireccionEntrega() != null) {
            pedido.setDireccionEntrega(dto.getDireccionEntrega().trim());
        }

        if (dto.getNotas() != null) {
            pedido.setNotas(dto.getNotas().trim());
        }

        return toResponseDTO(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponseDTO actualizarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = buscarPorId(id);
        pedido.setEstado(nuevoEstado);
        return toResponseDTO(pedidoRepository.save(pedido));
    }

    @Transactional
    public void eliminarPedido(Long id) {
        Pedido pedido = buscarPorId(id);
        pedido.setEliminado(true);
        pedidoRepository.save(pedido);
    }

    private PedidoResponseDTO toResponseDTO(Pedido pedido) {

        List<DetallePedidoResponseDTO> detalles = pedido.getDetalles()
                .stream()
                .map(d -> {
                    Producto producto = d.getProducto();

                    ProductoPedidoResponseDTO productoDTO = new ProductoPedidoResponseDTO(
                            producto.getId(),
                            producto.getNombre(),
                            producto.getPrecio(),
                            producto.getStock(),
                            producto.getImagen(),
                            producto.getDisponible()
                    );

                    return new DetallePedidoResponseDTO(
                            d.getId(),
                            d.getCantidad(),
                            d.getSubtotal(),
                            productoDTO
                    );
                })
                .toList();

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getUsuario().getId(),
                pedido.getUsuario().getNombre() + " " + pedido.getUsuario().getApellido(),
                pedido.getFecha(),
                pedido.getEstado(),
                pedido.getFormaPago(),
                pedido.getTotal(),
                pedido.getTelefono(),
                pedido.getDireccionEntrega(),
                pedido.getNotas(),
                detalles
        );
    }
}