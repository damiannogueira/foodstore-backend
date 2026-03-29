package foodstore_backend.service;

import foodstore_backend.dto.UsuarioCreateDTO;
import foodstore_backend.dto.UsuarioResponseDTO;
import foodstore_backend.exception.DuplicateResourceException;
import foodstore_backend.model.Usuario;
import foodstore_backend.model.enums.Rol;
import foodstore_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

// Servicio que contiene la lógica de negocio para manejar usuarios
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public UsuarioResponseDTO obtenerPorId(Long id) {
        return toResponseDTO(buscarEntidadPorId(id));
    }

    public Usuario buscarEntidadPorId(Long id) {
        return usuarioRepository.findByIdOrThrow(id, "Usuario");
    }

    public Usuario buscarEntidadPorEmail(String email) {
        return usuarioRepository.findByEmailAndEliminadoFalse(email)
                .orElseThrow(() -> new foodstore_backend.exception.ResourceNotFoundException(
                        "Usuario no encontrado con email: " + email
                ));
    }

    // Registra un usuario nuevo con mail único y contraseña hasheada
    public UsuarioResponseDTO registrarUsuario(UsuarioCreateDTO usuarioCreateDTO) {

        usuarioRepository.findByEmailAndEliminadoFalse(usuarioCreateDTO.getEmail())
                .ifPresent(u -> {
                    throw new DuplicateResourceException(
                            "Ya existe un usuario con el email: " + usuarioCreateDTO.getEmail()
                    );
                });

        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioCreateDTO.getNombre());
        usuario.setApellido(usuarioCreateDTO.getApellido());
        usuario.setEmail(usuarioCreateDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(usuarioCreateDTO.getPassword()));
        usuario.setRol(Rol.USUARIO);
        usuario.setEliminado(false);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return toResponseDTO(usuarioGuardado);
    }

    // Realiza baja lógica sin eliminar físicamente el usuario
    public void eliminarUsuario(Long id) {
        Usuario usuario = buscarEntidadPorId(id);
        usuario.setEliminado(true);
        usuarioRepository.save(usuario);
    }

    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }
}