package foodstore_backend.service;

import foodstore_backend.dto.DetallePedidoResponseDTO;
import foodstore_backend.dto.PedidoCreateDTO;
import foodstore_backend.dto.PedidoDetalleCreateDTO;
import foodstore_backend.dto.PedidoEditDTO;
import foodstore_backend.dto.PedidoResponseDTO;
import foodstore_backend.dto.ProductoPedidoResponseDTO;
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

    // Devuelve todos los pedidos activos
    public List<PedidoResponseDTO> listarPedidos() {
        return pedidoRepository.findByEliminadoFalseOrderByFechaDesc()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Devuelve los pedidos activos de un usuario
    public List<PedidoResponseDTO> listarPedidosPorUsuario(Long usuarioId) {
        usuarioService.buscarEntidadPorId(usuarioId);

        return pedidoRepository.findByUsuarioIdAndEliminadoFalseOrderByFechaDesc(usuarioId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Devuelve los pedidos activos filtrados por estado
    public List<PedidoResponseDTO> listarPedidosPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstadoAndEliminadoFalseOrderByFechaDesc(estado)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Busca un pedido activo por id o lanza excepción si no existe
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido no encontrado con id: " + id
                ));
    }

    // Obtiene un pedido activo por id y lo convierte a DTO de respuesta
    public PedidoResponseDTO obtenerPorId(Long id) {
        return toResponseDTO(buscarPorId(id));
    }

    // Guarda un nuevo pedido, valida stock y descuenta unidades
    @Transactional
    public PedidoResponseDTO guardarPedido(PedidoCreateDTO dto) {

        // Validación defensiva por si llega una lista vacía al service
        if (dto.detalles() == null || dto.detalles().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un detalle");
        }

        Usuario usuario = usuarioService.buscarEntidadPorId(dto.idUsuario());

        List<Producto> productosValidados = new ArrayList<>();
        List<PedidoDetalleCreateDTO> items = dto.detalles();

        // Primero validamos todos los productos antes de descontar stock
        for (PedidoDetalleCreateDTO item : items) {
            Producto producto = productoService.buscarPorId(item.productoId());

            if (!Boolean.TRUE.equals(producto.getDisponible())) {
                throw new IllegalArgumentException(
                        "El producto '" + producto.getNombre() + "' no está disponible para la venta"
                );
            }

            if (producto.getStock() < item.cantidad()) {
                throw new InsufficientStockException(
                        "Stock insuficiente para '" + producto.getNombre()
                                + "'. Disponible: " + producto.getStock()
                                + ", Solicitado: " + item.cantidad()
                );
            }

            productosValidados.add(producto);
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFormaPago(dto.formaPago());
        pedido.setTelefono(dto.telefono().trim());
        pedido.setDireccionEntrega(dto.direccionEntrega().trim());
        pedido.setNotas(dto.notas() != null && !dto.notas().trim().isEmpty() ? dto.notas().trim() : null);
        pedido.setEliminado(false);

        BigDecimal total = BigDecimal.ZERO;

        // Creamos detalles, calculamos subtotales y recién ahí descontamos stock
        for (int i = 0; i < items.size(); i++) {
            PedidoDetalleCreateDTO item = items.get(i);
            Producto producto = productosValidados.get(i);

            BigDecimal subtotal = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(item.cantidad()));

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.cantidad());
            detalle.setSubtotal(subtotal);
            detalle.setEliminado(false);

            pedido.getDetalles().add(detalle);

            producto.setStock(producto.getStock() - item.cantidad());

            total = total.add(subtotal);
        }

        pedido.setTotal(total);

        Pedido guardado = pedidoRepository.save(pedido);
        return toResponseDTO(guardado);
    }

    // Actualiza parcialmente estado y/o forma de pago
    @Transactional
    public PedidoResponseDTO actualizarPedido(Long id, PedidoEditDTO dto) {
        Pedido pedido = buscarPorId(id);

        if (dto.estado() != null) {
            pedido.setEstado(dto.estado());
        }

        if (dto.formaPago() != null) {
            pedido.setFormaPago(dto.formaPago());
        }

        Pedido pedidoActualizado = pedidoRepository.save(pedido);
        return toResponseDTO(pedidoActualizado);
    }

    // Actualiza solo el estado del pedido
    @Transactional
    public PedidoResponseDTO actualizarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = buscarPorId(id);
        pedido.setEstado(nuevoEstado);
        return toResponseDTO(pedidoRepository.save(pedido));
    }

    // Realiza soft delete del pedido
    @Transactional
    public void eliminarPedido(Long id) {
        Pedido pedido = buscarPorId(id);
        pedido.setEliminado(true);
        pedidoRepository.save(pedido);
    }

    // Convierte la entidad Pedido a DTO de respuesta
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