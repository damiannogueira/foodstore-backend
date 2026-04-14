package foodstore_backend.service;

import foodstore_backend.dto.LoginRequestDTO;
import foodstore_backend.dto.LoginResponseDTO;
import foodstore_backend.dto.UsuarioCreateDTO;
import foodstore_backend.dto.UsuarioResponseDTO;
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

    // Valida credenciales de acceso y devuelve los datos del usuario autenticado
    public LoginResponseDTO login(LoginRequestDTO dto) {

        Usuario usuario = usuarioService.buscarEntidadPorEmail(dto.email());

        if (!passwordEncoder.matches(dto.password(), usuario.getPassword())) {
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

    // Registra un nuevo usuario cliente y devuelve una respuesta equivalente al login
    public LoginResponseDTO register(UsuarioCreateDTO dto) {

        UsuarioResponseDTO usuarioCreado = usuarioService.crearUsuario(dto);

        return new LoginResponseDTO(
                usuarioCreado.id(),
                usuarioCreado.nombre(),
                usuarioCreado.apellido(),
                usuarioCreado.email(),
                usuarioCreado.rol(),
                "Registro exitoso"
        );
    }
}
