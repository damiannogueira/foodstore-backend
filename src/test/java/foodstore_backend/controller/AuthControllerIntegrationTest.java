package foodstore_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import foodstore_backend.dto.LoginRequestDTO;
import foodstore_backend.model.Usuario;
import foodstore_backend.model.enums.Rol;
import foodstore_backend.repository.CategoriaRepository;
import foodstore_backend.repository.DetallePedidoRepository;
import foodstore_backend.repository.PedidoRepository;
import foodstore_backend.repository.ProductoRepository;
import foodstore_backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Levanta el contexto completo de Spring para probar el endpoint real
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Antes de cada test preparo un usuario real en la base
    @BeforeEach
    void setUp() {
        // Limpio datos para evitar conflictos entre ejecuciones
        detallePedidoRepository.deleteAll();
        pedidoRepository.deleteAll();
        productoRepository.deleteAll();
        categoriaRepository.deleteAll();
        usuarioRepository.deleteAll();

        // =========================
        // 1. Crear usuario de prueba
        // =========================
        Usuario usuario = new Usuario();
        usuario.setNombre("Damian");
        usuario.setApellido("Nogueira");
        usuario.setEmail("testlogin@email.com");
        usuario.setCelular("3415551234");
        usuario.setPassword(passwordEncoder.encode("123456"));
        usuario.setRol(Rol.USUARIO);
        usuario.setEliminado(false);

        usuarioRepository.save(usuario);
    }

    @Test
    void loginExitoso() throws Exception {

        // Armo el JSON de login con el usuario que cargué en el setUp
        LoginRequestDTO request = new LoginRequestDTO(
                "testlogin@email.com",
                "123456"
        );

        // Hago el POST real al endpoint y valido respuesta
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("testlogin@email.com"))
                .andExpect(jsonPath("$.mensaje").value("Login exitoso"));
    }

    @Test
    void loginConPasswordIncorrecta() throws Exception {

        // Armo el JSON de login con password incorrecta
        LoginRequestDTO request = new LoginRequestDTO(
                "testlogin@email.com",
                "000000"
        );

        // Hago el POST real al endpoint y espero error por credenciales inválidas
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }
}