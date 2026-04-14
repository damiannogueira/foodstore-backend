package foodstore_backend.controller;

import foodstore_backend.dto.UsuarioCreateDTO;
import foodstore_backend.dto.UsuarioResponseDTO;
import foodstore_backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST para manejar usuarios
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @Operation(summary = "Obtener usuario por ID")
    @GetMapping("/{id}")
    public UsuarioResponseDTO obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    @Operation(summary = "Registrar nuevo usuario")
    @PostMapping
    public UsuarioResponseDTO crearUsuario(@Valid @RequestBody UsuarioCreateDTO dto) {
        return usuarioService.crearUsuario(dto);
    }

    @Operation(summary = "Eliminar usuario (soft delete)")
    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
    }
}