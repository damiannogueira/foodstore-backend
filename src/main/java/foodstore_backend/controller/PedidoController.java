package foodstore_backend.controller;

import foodstore_backend.dto.EstadoPedidoDTO;
import foodstore_backend.dto.PedidoCreateDTO;
import foodstore_backend.dto.PedidoEditDTO;
import foodstore_backend.dto.PedidoResponseDTO;
import foodstore_backend.model.enums.EstadoPedido;
import foodstore_backend.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador que expone endpoints para manejar pedidos
@RestController
@RequestMapping({"/api/orders"})
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidos(
            @RequestParam(required = false) EstadoPedido estado) {

        if (estado != null) {
            return ResponseEntity.ok(pedidoService.listarPedidosPorEstado(estado));
        }

        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> guardarPedido(
            @Valid @RequestBody PedidoCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoService.guardarPedido(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> actualizarPedido(
            @PathVariable Long id,
            @Valid @RequestBody PedidoEditDTO dto) {
        return ResponseEntity.ok(pedidoService.actualizarPedido(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidosPorUsuario(
            @PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoService.listarPedidosPorUsuario(usuarioId));
    }

    @PatchMapping({"/{id}/status", "/{id}/estado"})
    public ResponseEntity<PedidoResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoPedidoDTO dto) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, dto.getEstado()));
    }
}