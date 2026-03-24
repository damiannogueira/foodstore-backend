package foodstore_backend.service;

import foodstore_backend.exception.DuplicateResourceException;
import foodstore_backend.exception.ResourceNotFoundException;
import foodstore_backend.model.Usuario;
import foodstore_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Servicio que contiene la lógica de negocio para manejar usuarios
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Devuelve todos los usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Busca un usuario por ID o lanza excepción si no existe
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    // Busca usuario por email
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
    }

    // Guarda un usuario validando email duplicado
    public Usuario guardarUsuario(Usuario usuario) {

        // Validar email duplicado
        usuarioRepository.findByEmail(usuario.getEmail())
                .ifPresent(u -> {
                    throw new DuplicateResourceException("El email ya está registrado");
                });

        // Setear activo si viene null
        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }

        return usuarioRepository.save(usuario);
    }

    // Elimina usuario verificando que exista
    public void eliminarUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        usuarioRepository.delete(usuario);
    }
}