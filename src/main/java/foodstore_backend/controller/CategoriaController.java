package foodstore_backend.controller;

import foodstore_backend.dto.CategoriaCreateDTO;
import foodstore_backend.dto.CategoriaEditDTO;
import foodstore_backend.dto.CategoriaResponseDTO;
import foodstore_backend.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST para manejar categorías
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Operation(summary = "Listar todas las categorías")
    @GetMapping
    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaService.listarCategorias();
    }

    @Operation(summary = "Obtener categoría por ID")
    @GetMapping("/{id}")
    public CategoriaResponseDTO obtenerPorId(@PathVariable Long id) {
        return categoriaService.obtenerPorId(id);
    }

    @Operation(summary = "Crear una nueva categoría")
    @PostMapping
    public CategoriaResponseDTO guardarCategoria(@Valid @RequestBody CategoriaCreateDTO dto) {
        return categoriaService.guardarCategoria(dto);
    }

    @Operation(summary = "Actualizar categoría")
    @PutMapping("/{id}")
    public CategoriaResponseDTO actualizarCategoria(@PathVariable Long id,
                                                    @RequestBody CategoriaEditDTO dto) {
        return categoriaService.actualizarCategoria(id, dto);
    }

    @Operation(summary = "Eliminar categoría (soft delete)")
    @DeleteMapping("/{id}")
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
    }
}