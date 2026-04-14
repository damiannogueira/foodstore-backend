package foodstore_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import foodstore_backend.dto.UsuarioCreateDTO;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Levanta el contexto completo de Spring para probar el endpoint real
@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerIntegrationTest {

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

    private Usuario usuarioPrueba;

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
        usuario.setEmail("usuario_test@email.com");
        usuario.setCelular("3415551234");
        usuario.setPassword(passwordEncoder.encode("123456"));
        usuario.setRol(Rol.USUARIO);
        usuario.setEliminado(false);

        usuarioPrueba = usuarioRepository.save(usuario);
    }

    @Test
    void listarUsuariosExitoso() throws Exception {
        // Hago el GET real al endpoint para listar usuarios
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value("usuario_test@email.com"));
    }

    @Test
    void obtenerUsuarioPorIdExitoso() throws Exception {
        // Hago el GET real al endpoint usando el id real guardado en setUp
        mockMvc.perform(get("/api/users/{id}", usuarioPrueba.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioPrueba.getId()))
                .andExpect(jsonPath("$.nombre").value("Damian"))
                .andExpect(jsonPath("$.apellido").value("Nogueira"))
                .andExpect(jsonPath("$.email").value("usuario_test@email.com"));
    }

    @Test
    void crearUsuarioExitoso() throws Exception {
        // Creo un nuevo usuario para enviarlo al endpoint
        UsuarioCreateDTO dto = new UsuarioCreateDTO(
                "Juan",
                "Perez",
                "juan_test@email.com",
                "3415559999",
                "123456"
        );

        // Hago el POST real al endpoint
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andExpect(jsonPath("$.email").value("juan_test@email.com"))
                .andExpect(jsonPath("$.celular").value("3415559999"));
    }

    @Test
    void crearUsuarioDuplicado() throws Exception {
        // Creo un usuario con el mismo email que el ya guardado en setUp
        UsuarioCreateDTO dto = new UsuarioCreateDTO(
                "Otro",
                "Usuario",
                "usuario_test@email.com",
                "3415551111",
                "123456"
        );

        // Hago el POST real al endpoint y espero conflicto por duplicado
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerUsuarioInexistente() throws Exception {
        // Hago el GET con un id inexistente y espero not found
        mockMvc.perform(get("/api/users/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarUsuarioSoftDelete() throws Exception {
        // Hago el DELETE real al endpoint usando el id real guardado en setUp
        mockMvc.perform(delete("/api/users/{id}", usuarioPrueba.getId()))
                .andExpect(status().isOk());

        // Verifico que ya no se pueda obtener porque quedó eliminado lógicamente
        mockMvc.perform(get("/api/users/{id}", usuarioPrueba.getId()))
                .andExpect(status().isNotFound());
    }
}