package foodstore_backend.service;

import foodstore_backend.dto.UsuarioCreateDTO;
import foodstore_backend.dto.UsuarioResponseDTO;
import foodstore_backend.exception.DuplicateResourceException;
import foodstore_backend.model.Usuario;
import foodstore_backend.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// Tests unitarios para UsuarioService
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void registrarUsuarioExitoso() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO(
                "Juan",
                "Perez",
                "juan@email.com",
                "123456789",
                "123456"
        );

        when(usuarioRepository.findByEmailAndEliminadoFalse("juan@email.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("123456")).thenReturn("hash-bcrypt");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setNombre("Juan");
        usuarioGuardado.setApellido("Perez");
        usuarioGuardado.setEmail("juan@email.com");
        usuarioGuardado.setCelular("123456789");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        UsuarioResponseDTO response = usuarioService.registrarUsuario(dto);

        assertNotNull(response);
        assertEquals("Juan", response.nombre());
        assertEquals("Perez", response.apellido());
        assertEquals("juan@email.com", response.email());
        assertEquals("123456789", response.celular());
    }

    @Test
    void registrarUsuarioConEmailDuplicado() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO(
                "Juan",
                "Perez",
                "juan@email.com",
                null,
                "123456"
        );

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setEmail("juan@email.com");

        when(usuarioRepository.findByEmailAndEliminadoFalse("juan@email.com"))
                .thenReturn(Optional.of(usuarioExistente));

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> usuarioService.registrarUsuario(dto)
        );

        assertEquals("Ya existe un usuario con el email: juan@email.com", exception.getMessage());
    }
}