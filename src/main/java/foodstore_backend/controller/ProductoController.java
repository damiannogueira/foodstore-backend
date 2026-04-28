package foodstore_backend.controller;

import foodstore_backend.dto.ProductoCreateDTO;
import foodstore_backend.dto.ProductoEditDTO;
import foodstore_backend.dto.ProductoResponseDTO;
import foodstore_backend.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST para manejar productos
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Operation(summary = "Listar todos los productos")
    @GetMapping
    public List<ProductoResponseDTO> listarProductos() {
        return productoService.listarProductos();
    }

    @Operation(summary = "Obtener producto por ID")
    @GetMapping("/{id}")
    public ProductoResponseDTO obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }

    @Operation(summary = "Listar productos disponibles")
    @GetMapping("/available")
    public List<ProductoResponseDTO> listarDisponibles() {
        return productoService.listarDisponibles();
    }

    @Operation(summary = "Listar productos por categoría")
    @GetMapping("/categoria/{categoriaId}")
    public List<ProductoResponseDTO> listarPorCategoria(@PathVariable Long categoriaId) {
        return productoService.listarPorCategoria(categoriaId);
    }

    @Operation(summary = "Crear un nuevo producto")
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> guardarProducto(@Valid @RequestBody ProductoCreateDTO dto) {
        // Devuelve 201 Created porque se crea un nuevo producto
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.guardarProducto(dto));
    }

    @Operation(summary = "Actualizar producto")
    @PutMapping("/{id}")
    public ProductoResponseDTO actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoEditDTO dto) {
        // Con @Valid aseguro que se validen los datos antes de actualizar
        return productoService.actualizarProducto(id, dto);
    }

    @Operation(summary = "Eliminar producto (soft delete)")
    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
    }
}