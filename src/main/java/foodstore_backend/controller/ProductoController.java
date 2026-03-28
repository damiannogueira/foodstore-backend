package foodstore_backend.controller;

import foodstore_backend.dto.ProductoCreateDTO;
import foodstore_backend.dto.ProductoEditDTO;
import foodstore_backend.dto.ProductoResponseDTO;
import foodstore_backend.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador que expone endpoints para manejar productos
@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> guardarProducto(
            @Valid @RequestBody ProductoCreateDTO productoCreateDTO) {
        ProductoResponseDTO productoGuardado = productoService.guardarProducto(productoCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoEditDTO productoEditDTO) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, productoEditDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoResponseDTO>> listarProductosPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.listarProductosPorCategoria(categoriaId));
    }
}