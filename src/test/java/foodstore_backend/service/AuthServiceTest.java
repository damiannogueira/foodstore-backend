package foodstore_backend.service;

import foodstore_backend.dto.LoginRequestDTO;
import foodstore_backend.dto.LoginResponseDTO;
import foodstore_backend.exception.ResourceNotFoundException;
import foodstore_backend.model.Usuario;
import foodstore_backend.model.enums.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

// Tests unitarios para AuthService
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNombre("Damian");
        usuario.setApellido("Nogueira");
        usuario.setEmail("damian@email.com");
        usuario.setPassword("hash");
        usuario.setRol(Rol.USUARIO);

        loginRequestDTO = new LoginRequestDTO("damian@email.com", "123456");
    }

    @Test
    void loginExitoso() {
        // Simulo que el usuario existe y que la password coincide
        when(usuarioService.buscarEntidadPorEmail("damian@email.com")).thenReturn(usuario);
        when(passwordEncoder.matches("123456", "hash")).thenReturn(true);

        LoginResponseDTO response = authService.login(loginRequestDTO);

        assertNotNull(response);
        assertEquals("Damian", response.nombre());
        assertEquals("Nogueira", response.apellido());
        assertEquals("damian@email.com", response.email());
        assertEquals(Rol.USUARIO, response.rol());
        assertEquals("Login exitoso", response.mensaje());
    }

    @Test
    void loginConPasswordIncorrecta() {
        // Simulo que el usuario existe pero la password no coincide
        when(usuarioService.buscarEntidadPorEmail("damian@email.com")).thenReturn(usuario);
        when(passwordEncoder.matches("123456", "hash")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.login(loginRequestDTO)
        );

        assertEquals("Credenciales inválidas", exception.getMessage());
    }

    @Test
    void loginConUsuarioInexistente() {
        // Simulo que no existe ningún usuario con ese email
        when(usuarioService.buscarEntidadPorEmail("damian@email.com"))
                .thenThrow(new ResourceNotFoundException("Usuario no encontrado con email: damian@email.com"));

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> authService.login(loginRequestDTO)
        );

        assertEquals("Usuario no encontrado con email: damian@email.com", exception.getMessage());
    }
}
