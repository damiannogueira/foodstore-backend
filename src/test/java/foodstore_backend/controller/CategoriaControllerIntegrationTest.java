package foodstore_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import foodstore_backend.dto.CategoriaCreateDTO;
import foodstore_backend.dto.CategoriaEditDTO;
import foodstore_backend.model.Categoria;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Levanta el contexto completo de Spring para probar el endpoint real
@SpringBootTest
@AutoConfigureMockMvc
class CategoriaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Categoria categoriaPrueba;

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
    }

    @Test
    void listarCategoriasExitoso() throws Exception {
        // Hago el GET real al endpoint para listar categorías
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("Hamburguesas"));
    }

    @Test
    void obtenerCategoriaPorIdExitoso() throws Exception {
        // Hago el GET real al endpoint usando el id real guardado en setUp
        mockMvc.perform(get("/api/categories/{id}", categoriaPrueba.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoriaPrueba.getId()))
                .andExpect(jsonPath("$.nombre").value("Hamburguesas"))
                .andExpect(jsonPath("$.descripcion").value("Categoría de hamburguesas"))
                .andExpect(jsonPath("$.imagen").value("https://misitio.com/hamburguesas.jpg"));
    }

    @Test
    void crearCategoriaExitoso() throws Exception {
        // Creo una nueva categoría para enviarla al endpoint
        CategoriaCreateDTO dto = new CategoriaCreateDTO(
                "Pizzas",
                "Categoría de pizzas",
                "https://misitio.com/pizzas.jpg"
        );

        // Hago el POST real al endpoint
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pizzas"))
                .andExpect(jsonPath("$.descripcion").value("Categoría de pizzas"))
                .andExpect(jsonPath("$.imagen").value("https://misitio.com/pizzas.jpg"));
    }

    @Test
    void crearCategoriaDuplicada() throws Exception {
        // Creo una categoría con el mismo nombre que la ya guardada en setUp
        CategoriaCreateDTO dto = new CategoriaCreateDTO(
                "Hamburguesas",
                "Otra descripción",
                "https://misitio.com/otra.jpg"
        );

        // Hago el POST real al endpoint y espero conflicto por duplicado
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarCategoriaExitoso() throws Exception {
        // Creo el DTO de edición usando un nuevo nombre y nuevos datos
        CategoriaEditDTO dto = new CategoriaEditDTO(
                "Empanadas",
                "Categoría de empanadas",
                "https://misitio.com/empanadas.jpg"
        );

        // Hago el PUT real al endpoint usando el id real guardado en setUp
        mockMvc.perform(put("/api/categories/{id}", categoriaPrueba.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Empanadas"))
                .andExpect(jsonPath("$.descripcion").value("Categoría de empanadas"))
                .andExpect(jsonPath("$.imagen").value("https://misitio.com/empanadas.jpg"));
    }

    @Test
    void obtenerCategoriaInexistente() throws Exception {
        // Hago el GET con un id inexistente y espero not found
        mockMvc.perform(get("/api/categories/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarCategoriaSoftDelete() throws Exception {
        // Hago el DELETE real al endpoint usando el id real guardado en setUp
        mockMvc.perform(delete("/api/categories/{id}", categoriaPrueba.getId()))
                .andExpect(status().isOk());

        // Verifico que ya no se pueda obtener porque quedó eliminada lógicamente
        mockMvc.perform(get("/api/categories/{id}", categoriaPrueba.getId()))
                .andExpect(status().isNotFound());
    }
}