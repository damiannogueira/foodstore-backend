package foodstore_backend.controller;

import foodstore_backend.dto.PedidoCreateDTO;
import foodstore_backend.dto.PedidoEditDTO;
import foodstore_backend.dto.PedidoResponseDTO;
import foodstore_backend.model.enums.EstadoPedido;
import foodstore_backend.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST para manejar pedidos
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Listar todos los pedidos")
    @GetMapping
    public List<PedidoResponseDTO> listarPedidos() {
        return pedidoService.listarPedidos();
    }

    @Operation(summary = "Listar pedidos por usuario")
    @GetMapping("/usuario/{usuarioId}")
    public List<PedidoResponseDTO> listarPedidosPorUsuario(@PathVariable Long usuarioId) {
        return pedidoService.listarPedidosPorUsuario(usuarioId);
    }

    @Operation(summary = "Listar pedidos por estado")
    @GetMapping("/estado/{estado}")
    public List<PedidoResponseDTO> listarPedidosPorEstado(@PathVariable EstadoPedido estado) {
        return pedidoService.listarPedidosPorEstado(estado);
    }

    @Operation(summary = "Obtener pedido por ID")
    @GetMapping("/{id}")
    public PedidoResponseDTO obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id);
    }

    @Operation(summary = "Crear un nuevo pedido")
    @PostMapping
    public PedidoResponseDTO guardarPedido(@Valid @RequestBody PedidoCreateDTO dto) {
        return pedidoService.guardarPedido(dto);
    }

    @Operation(summary = "Actualizar pedido (estado o forma de pago)")
    @PutMapping("/{id}")
    public PedidoResponseDTO actualizarPedido(@PathVariable Long id,
                                              @RequestBody PedidoEditDTO dto) {
        return pedidoService.actualizarPedido(id, dto);
    }

    @Operation(summary = "Actualizar solo el estado del pedido")
    @PatchMapping("/{id}/status")
    public PedidoResponseDTO actualizarEstado(@PathVariable Long id,
                                              @RequestParam EstadoPedido estado) {
        return pedidoService.actualizarEstado(id, estado);
    }

    @Operation(summary = "Eliminar pedido (soft delete)")
    @DeleteMapping("/{id}")
    public void eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
    }
}