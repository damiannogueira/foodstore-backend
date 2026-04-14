package foodstore_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import foodstore_backend.dto.PedidoCreateDTO;
import foodstore_backend.dto.PedidoDetalleCreateDTO;
import foodstore_backend.model.Categoria;
import foodstore_backend.model.Producto;
import foodstore_backend.model.Usuario;
import foodstore_backend.model.enums.FormaPago;
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

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Levanta el contexto completo de Spring para probar el endpoint real
@SpringBootTest
@AutoConfigureMockMvc
class PedidoControllerIntegrationTest {

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
    private Producto productoPrueba;

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
        usuario.setEmail("pedido_test@email.com");
        usuario.setCelular("3415551234");
        usuario.setPassword(passwordEncoder.encode("123456"));
        usuario.setRol(Rol.USUARIO);
        usuario.setEliminado(false);

        usuarioPrueba = usuarioRepository.save(usuario);

        // =========================
        // 2. Crear categoría de prueba
        // =========================
        Categoria categoria = new Categoria();
        categoria.setNombre("Hamburguesas Test");
        categoria.setDescripcion("Categoría para test de integración");
        categoria.setImagen("https://misitio.com/categoria-test.jpg");
        categoria.setEliminado(false);

        Categoria categoriaGuardada = categoriaRepository.save(categoria);

        // =========================
        // 3. Crear producto de prueba
        // =========================
        Producto producto = new Producto();
        producto.setNombre("Hamburguesa Test");
        producto.setDescripcion("Producto para test de integración");
        producto.setPrecio(new BigDecimal("8500"));
        producto.setStock(10);
        producto.setImagen("https://misitio.com/producto-test.jpg");
        producto.setDisponible(true);
        producto.setCategoria(categoriaGuardada);
        producto.setEliminado(false);

        productoPrueba = productoRepository.save(producto);
    }

    @Test
    void crearPedidoExitoso() throws Exception {

        // Creo el detalle del pedido usando el id real del producto guardado en setUp
        PedidoDetalleCreateDTO detalle = new PedidoDetalleCreateDTO(
                productoPrueba.getId(),
                2
        );

        // Creo el pedido usando el id real del usuario guardado en setUp
        PedidoCreateDTO pedido = new PedidoCreateDTO(
                usuarioPrueba.getId(),
                FormaPago.TRANSFERENCIA,
                "3415551234",
                "Av Siempre Viva 123",
                "Sin cebolla",
                List.of(detalle)
        );

        // Hago el POST real al endpoint
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.detalles").isArray())
                .andExpect(jsonPath("$.telefono").value("3415551234"))
                .andExpect(jsonPath("$.direccionEntrega").value("Av Siempre Viva 123"))
                .andExpect(jsonPath("$.notas").value("Sin cebolla"));
    }
}
