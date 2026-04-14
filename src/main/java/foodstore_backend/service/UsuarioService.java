package foodstore_backend.service;

import foodstore_backend.dto.UsuarioCreateDTO;
import foodstore_backend.dto.UsuarioEditDTO;
import foodstore_backend.dto.UsuarioResponseDTO;
import foodstore_backend.exception.DuplicateResourceException;
import foodstore_backend.exception.ResourceNotFoundException;
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
        return usuarioRepository.findAllByEliminadoFalse()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public UsuarioResponseDTO obtenerPorId(Long id) {
        return toResponseDTO(buscarEntidadPorId(id));
    }

    public Usuario buscarEntidadPorId(Long id) {
        return usuarioRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con id: " + id
                ));
    }

    public Usuario buscarEntidadPorEmail(String email) {
        return usuarioRepository.findByEmailAndEliminadoFalse(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con email: " + email
                ));
    }

    // Registra un usuario nuevo con mail único y contraseña encriptada
    public UsuarioResponseDTO crearUsuario(UsuarioCreateDTO usuarioCreateDTO) {

        usuarioRepository.findByEmailAndEliminadoFalse(usuarioCreateDTO.email())
                .ifPresent(u -> {
                    throw new DuplicateResourceException(
                            "Ya existe un usuario con el email: " + usuarioCreateDTO.email()
                    );
                });

        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioCreateDTO.nombre());
        usuario.setApellido(usuarioCreateDTO.apellido());
        usuario.setEmail(usuarioCreateDTO.email());
        usuario.setCelular(usuarioCreateDTO.celular());
        usuario.setPassword(passwordEncoder.encode(usuarioCreateDTO.password()));
        usuario.setRol(Rol.USUARIO);
        usuario.setEliminado(false);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return toResponseDTO(usuarioGuardado);
    }

    // Actualiza solo los campos enviados
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioEditDTO usuarioEditDTO) {
        Usuario usuario = buscarEntidadPorId(id);

        if (usuarioEditDTO.nombre() != null && !usuarioEditDTO.nombre().trim().isEmpty()) {
            usuario.setNombre(usuarioEditDTO.nombre());
        }

        if (usuarioEditDTO.apellido() != null && !usuarioEditDTO.apellido().trim().isEmpty()) {
            usuario.setApellido(usuarioEditDTO.apellido());
        }

        if (usuarioEditDTO.email() != null && !usuarioEditDTO.email().trim().isEmpty()) {
            String nuevoEmail = usuarioEditDTO.email();

            usuarioRepository.findByEmailAndEliminadoFalse(nuevoEmail)
                    .ifPresent(u -> {
                        if (!u.getId().equals(usuario.getId())) {
                            throw new DuplicateResourceException(
                                    "Ya existe un usuario con el email: " + nuevoEmail
                            );
                        }
                    });

            usuario.setEmail(nuevoEmail);
        }

        if (usuarioEditDTO.celular() != null) {
            usuario.setCelular(usuarioEditDTO.celular());
        }

        if (usuarioEditDTO.password() != null && !usuarioEditDTO.password().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioEditDTO.password()));
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return toResponseDTO(usuarioActualizado);
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
                usuario.getCelular(),
                usuario.getRol()
        );
    }
}