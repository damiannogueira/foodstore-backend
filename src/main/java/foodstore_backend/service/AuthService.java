package foodstore_backend.service;

import foodstore_backend.dto.LoginRequestDTO;
import foodstore_backend.dto.LoginResponseDTO;
import foodstore_backend.exception.ResourceNotFoundException;
import foodstore_backend.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Servicio para autenticación básica del sistema
@Service
public class AuthService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO dto) {

        Usuario usuario = usuarioService.buscarEntidadPorEmail(dto.getEmail());

        if (Boolean.TRUE.equals(usuario.getEliminado())) {
            throw new ResourceNotFoundException("Usuario no encontrado con email: " + dto.getEmail());
        }

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        return new LoginResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol(),
                "Login exitoso"
        );
    }
}
