package foodstore_backend.controller;

import foodstore_backend.dto.CategoriaCreateDTO;
import foodstore_backend.dto.CategoriaEditDTO;
import foodstore_backend.dto.CategoriaResponseDTO;
import foodstore_backend.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador que expone endpoints para manejar categorías
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> guardarCategoria(
            @Valid @RequestBody CategoriaCreateDTO categoriaCreateDTO) {
        CategoriaResponseDTO categoriaGuardada = categoriaService.guardarCategoria(categoriaCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaGuardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizarCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaEditDTO categoriaEditDTO) {
        return ResponseEntity.ok(categoriaService.actualizarCategoria(id, categoriaEditDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}