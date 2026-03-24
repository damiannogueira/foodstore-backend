package foodstore_backend.controller;

import foodstore_backend.model.Categoria;
import foodstore_backend.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador que expone endpoints para manejar categorías
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // Obtener todas las categorías
    @GetMapping
    public List<Categoria> listarCategorias() {
        return categoriaService.listarCategorias();
    }

    // Obtener categoría por ID
    @GetMapping("/{id}")
    public Categoria obtenerPorId(@PathVariable Long id) {
        return categoriaService.buscarPorId(id);
    }

    // Crear categoría
    @PostMapping
    public Categoria crearCategoria(@Valid @RequestBody Categoria categoria) {
        return categoriaService.guardarCategoria(categoria);
    }

    // Eliminar categoría
    @DeleteMapping("/{id}")
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
    }

    // Actualizar categoría
    @PutMapping("/{id}")
    public Categoria actualizarCategoria(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        return categoriaService.actualizarCategoria(id, categoria);
    }
}
