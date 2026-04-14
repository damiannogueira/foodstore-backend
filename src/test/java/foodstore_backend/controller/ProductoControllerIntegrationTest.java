package foodstore_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import foodstore_backend.dto.ProductoCreateDTO;
import foodstore_backend.dto.ProductoEditDTO;
import foodstore_backend.model.Categoria;
import foodstore_backend.model.Producto;
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
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Levanta el contexto completo de Spring para probar el endpoint real
@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Categoria categoriaPrueba;
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
        // 1. Crear categoría de prueba
        // =========================
        Categoria categoria = new Categoria();
        categoria.setNombre("Hamburguesas");
        categoria.setDescripcion("Categoría de hamburguesas");
        categoria.setImagen("https://misitio.com/hamburguesas.jpg");
        categoria.setEliminado(false);

        categoriaPrueba = categoriaRepository.save(categoria);

        // =========================
        // 2. Crear producto de prueba
        // =========================
        Producto producto = new Producto();
        producto.setNombre("Hamburguesa Test");
        producto.setDescripcion("Producto para test de integración");
        producto.setPrecio(new BigDecimal("8500"));
        producto.setStock(10);
        producto.setImagen("https://misitio.com/producto-test.jpg");
        producto.setDisponible(true);
        producto.setCategoria(categoriaPrueba);
        producto.setEliminado(false);

        productoPrueba = productoRepository.save(producto);
    }

    @Test
    void listarProductosExitoso() throws Exception {
        // Hago el GET real al endpoint para listar productos
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("Hamburguesa Test"));
    }

    @Test
    void obtenerProductoPorIdExitoso() throws Exception {
        // Hago el GET real al endpoint usando el id real guardado en setUp
        mockMvc.perform(get("/api/products/{id}", productoPrueba.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productoPrueba.getId()))
                .andExpect(jsonPath("$.nombre").value("Hamburguesa Test"))
                .andExpect(jsonPath("$.descripcion").value("Producto para test de integración"))
                .andExpect(jsonPath("$.precio").value(8500))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.imagen").value("https://misitio.com/producto-test.jpg"))
                .andExpect(jsonPath("$.disponible").value(true));
    }

    @Test
    void crearProductoExitoso() throws Exception {
        // Creo un nuevo producto para enviarlo al endpoint
        ProductoCreateDTO dto = new ProductoCreateDTO(
                "Pizza Muzzarella",
                "Producto nuevo de prueba",
                new BigDecimal("9200"),
                15,
                "https://misitio.com/pizza.jpg",
                true,
                categoriaPrueba.getId()
        );

        // Hago el POST real al endpoint
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pizza Muzzarella"))
                .andExpect(jsonPath("$.descripcion").value("Producto nuevo de prueba"))
                .andExpect(jsonPath("$.precio").value(9200))
                .andExpect(jsonPath("$.stock").value(15))
                .andExpect(jsonPath("$.imagen").value("https://misitio.com/pizza.jpg"))
                .andExpect(jsonPath("$.disponible").value(true));
    }

    @Test
    void crearProductoDuplicado() throws Exception {
        // Creo un producto con el mismo nombre que el ya guardado en setUp
        ProductoCreateDTO dto = new ProductoCreateDTO(
                "Hamburguesa Test",
                "Otro producto",
                new BigDecimal("9000"),
                5,
                "https://misitio.com/otro.jpg",
                true,
                categoriaPrueba.getId()
        );

        // Hago el POST real al endpoint y espero conflicto por duplicado
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarProductoExitoso() throws Exception {
        // Creo el DTO de edición con nuevos datos
        ProductoEditDTO dto = new ProductoEditDTO(
                "Hamburguesa Doble",
                "Producto editado",
                new BigDecimal("9900"),
                20,
                "https://misitio.com/hamburguesa-doble.jpg",
                false,
                categoriaPrueba.getId()
        );

        // Hago el PUT real al endpoint usando el id real guardado en setUp
        mockMvc.perform(put("/api/products/{id}", productoPrueba.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Hamburguesa Doble"))
                .andExpect(jsonPath("$.descripcion").value("Producto editado"))
                .andExpect(jsonPath("$.precio").value(9900))
                .andExpect(jsonPath("$.stock").value(20))
                .andExpect(jsonPath("$.imagen").value("https://misitio.com/hamburguesa-doble.jpg"))
                .andExpect(jsonPath("$.disponible").value(false));
    }

    @Test
    void listarProductosDisponiblesExitoso() throws Exception {
        // Hago el GET real al endpoint de productos disponibles
        mockMvc.perform(get("/api/products/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("Hamburguesa Test"));
    }

    @Test
    void listarProductosPorCategoriaExitoso() throws Exception {
        // Hago el GET real al endpoint usando el id real de la categoría guardada en setUp
        mockMvc.perform(get("/api/products/categoria/{categoriaId}", categoriaPrueba.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("Hamburguesa Test"));
    }

    @Test
    void obtenerProductoInexistente() throws Exception {
        // Hago el GET con un id inexistente y espero not found
        mockMvc.perform(get("/api/products/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarProductoSoftDelete() throws Exception {
        // Hago el DELETE real al endpoint usando el id real guardado en setUp
        mockMvc.perform(delete("/api/products/{id}", productoPrueba.getId()))
                .andExpect(status().isOk());

        // Verifico que ya no se pueda obtener porque quedó eliminado lógicamente
        mockMvc.perform(get("/api/products/{id}", productoPrueba.getId()))
                .andExpect(status().isNotFound());
    }
}