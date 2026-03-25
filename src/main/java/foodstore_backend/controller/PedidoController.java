package foodstore_backend.controller;

import foodstore_backend.model.Pedido;
import foodstore_backend.service.PedidoService;
import foodstore_backend.dto.EstadoPedidoDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador que expone endpoints para manejar pedidos
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // Obtener todos los pedidos
    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoService.listarPedidos();
    }

    // Obtener pedido por ID
    @GetMapping("/{id}")
    public Pedido obtenerPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id);
    }

    // Obtener pedidos de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<Pedido> listarPorUsuario(@PathVariable Long usuarioId) {
        return pedidoService.listarPedidosPorUsuario(usuarioId);
    }

    // Crear pedido
    @PostMapping
    public Pedido crearPedido(@Valid @RequestBody Pedido pedido) {
        return pedidoService.guardarPedido(pedido);
    }

    // Eliminar pedido
    @DeleteMapping("/{id}")
    public void eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
    }

    // Actualizar estado de un pedido
    @PutMapping("/{id}/estado")
    public Pedido actualizarEstado(@PathVariable Long id, @Valid @RequestBody EstadoPedidoDTO estadoPedidoDTO) {
        return pedidoService.actualizarEstado(id, estadoPedidoDTO.getEstado());
    }
}