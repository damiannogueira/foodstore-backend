package foodstore_backend.service;

import foodstore_backend.exception.ResourceNotFoundException;
import foodstore_backend.exception.InsufficientStockException;
import foodstore_backend.model.DetallePedido;
import foodstore_backend.model.Pedido;
import foodstore_backend.model.Producto;
import foodstore_backend.model.Usuario;
import foodstore_backend.model.enums.EstadoPedido;
import foodstore_backend.repository.PedidoRepository;
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

    // Devuelve todos los pedidos
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    // Busca un pedido por ID o lanza excepción si no existe
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }

    // Guarda un pedido calculando total, subtotales, fecha y validando stock
    public Pedido guardarPedido(Pedido pedido) {

        Usuario usuario = usuarioService.buscarPorId(pedido.getUsuario().getId());
        pedido.setUsuario(usuario);

        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);

        BigDecimal total = BigDecimal.ZERO;

        for (DetallePedido detalle : pedido.getDetalles()) {

            Producto producto = productoService.buscarPorId(detalle.getProducto().getId());

            if (producto.getStock() < detalle.getCantidad()) {
                throw new InsufficientStockException("Stock insuficiente para el producto: " + producto.getNombre());
}
            BigDecimal subtotal = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad()));

            detalle.setProducto(producto);
            detalle.setPedido(pedido);
            detalle.setSubtotal(subtotal);

            producto.setStock(producto.getStock() - detalle.getCantidad());

            total = total.add(subtotal);
        }

        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    // Elimina un pedido verificando que exista
    public void eliminarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));

        pedidoRepository.delete(pedido);
    }

    // Devuelve los pedidos de un usuario
    public List<Pedido> listarPedidosPorUsuario(Long usuarioId) {
        usuarioService.buscarPorId(usuarioId);
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    // Actualiza el estado de un pedido existente
    public Pedido actualizarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));

        pedido.setEstado(nuevoEstado);

        return pedidoRepository.save(pedido);
    }
}
