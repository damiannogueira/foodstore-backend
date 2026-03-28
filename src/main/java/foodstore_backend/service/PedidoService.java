package foodstore_backend.service;

import foodstore_backend.dto.DetallePedidoResponseDTO;
import foodstore_backend.dto.PedidoCreateDTO;
import foodstore_backend.dto.PedidoDetalleCreateDTO;
import foodstore_backend.dto.PedidoEditDTO;
import foodstore_backend.dto.PedidoResponseDTO;
import foodstore_backend.exception.InsufficientStockException;
import foodstore_backend.exception.ResourceNotFoundException;
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
        return pedidoRepository.findByEliminadoFalse()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }

    public PedidoResponseDTO obtenerPorId(Long id) {
        return toResponseDTO(buscarPorId(id));
    }

    @Transactional
    public PedidoResponseDTO guardarPedido(PedidoCreateDTO dto) {

        Long usuarioId = dto.getUsuarioId();
        Usuario usuario = usuarioService.buscarEntidadPorId(usuarioId);

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFormaPago(dto.getFormaPago());
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setEliminado(false);

        BigDecimal total = BigDecimal.ZERO;

        for (PedidoDetalleCreateDTO item : dto.getDetalles()) {

            Long productoId = item.getProductoId();
            Producto producto = productoService.buscarPorId(productoId);

            if (producto.getStock() < item.getCantidad()) {
                throw new InsufficientStockException(
                        "Stock insuficiente para el producto: " + producto.getNombre()
                );
            }

            BigDecimal subtotal = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(item.getCantidad()));

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setPedido(pedido);
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

        if (dto.getFormaPago() != null) {
            pedido.setFormaPago(dto.getFormaPago());
        }

        return toResponseDTO(pedidoRepository.save(pedido));
    }

    @Transactional
    public void eliminarPedido(Long id) {
        Pedido pedido = buscarPorId(id);
        pedido.setEliminado(true);
        pedidoRepository.save(pedido);
    }

    public List<PedidoResponseDTO> listarPedidosPorUsuario(Long usuarioId) {
        usuarioService.buscarEntidadPorId(usuarioId);
        return pedidoRepository.findByUsuarioIdAndEliminadoFalse(usuarioId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public PedidoResponseDTO actualizarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = buscarPorId(id);
        pedido.setEstado(nuevoEstado);
        return toResponseDTO(pedidoRepository.save(pedido));
    }

    private PedidoResponseDTO toResponseDTO(Pedido pedido) {

        List<DetallePedidoResponseDTO> detalles = pedido.getDetalles()
                .stream()
                .map(d -> new DetallePedidoResponseDTO(
                        d.getProducto().getId(),
                        d.getProducto().getNombre(),
                        d.getCantidad(),
                        d.getSubtotal()
                ))
                .toList();

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getUsuario().getId(),
                pedido.getUsuario().getNombre() + " " + pedido.getUsuario().getApellido(),
                pedido.getFecha(),
                pedido.getEstado(),
                pedido.getFormaPago(),
                pedido.getTotal(),
                detalles
        );
    }
}
