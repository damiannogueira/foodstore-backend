package foodstore_backend.service;

import foodstore_backend.dto.ProductoCreateDTO;
import foodstore_backend.dto.ProductoResponseDTO;
import foodstore_backend.exception.DuplicateResourceException;
import foodstore_backend.exception.ResourceNotFoundException;
import foodstore_backend.model.Categoria;
import foodstore_backend.model.Producto;
import foodstore_backend.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Tests unitarios para ProductoService
@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void guardarProductoExitoso() {

        // =========================
        // 1. Crear DTO de entrada
        // =========================
        ProductoCreateDTO dto = new ProductoCreateDTO(
                "Hamburguesa",
                "Producto test",
                new BigDecimal("5000"),
                10,
                "https://misitio.com/img.jpg",
                true,
                1L
        );

        // =========================
        // 2. Mock de categoría
        // =========================
        Categoria categoria = new Categoria();
        categoria.setId(1L);

        // =========================
        // 3. Mock de producto guardado
        // =========================
        Producto productoGuardado = new Producto();
        productoGuardado.setId(1L);
        productoGuardado.setNombre("Hamburguesa");
        productoGuardado.setDescripcion("Producto test");
        productoGuardado.setPrecio(new BigDecimal("5000"));
        productoGuardado.setStock(10);
        productoGuardado.setImagen("https://misitio.com/img.jpg");
        productoGuardado.setDisponible(true);
        productoGuardado.setCategoria(categoria);

        // =========================
        // 4. Configurar mocks
        // =========================
        when(productoRepository.findByNombreIgnoreCaseAndEliminadoFalse("Hamburguesa"))
                .thenReturn(Optional.empty());

        when(categoriaService.buscarPorId(1L)).thenReturn(categoria);

        when(productoRepository.save(any(Producto.class)))
                .thenReturn(productoGuardado);

        // =========================
        // 5. Ejecutar método
        // =========================
        ProductoResponseDTO response = productoService.guardarProducto(dto);

        // =========================
        // 6. Validaciones
        // =========================
        assertNotNull(response);
        assertEquals("Hamburguesa", response.nombre());
        assertEquals(new BigDecimal("5000"), response.precio());
    }

    @Test
    void guardarProductoDuplicado() {

        // =========================
        // 1. Crear DTO duplicado
        // =========================
        ProductoCreateDTO dto = new ProductoCreateDTO(
                "Hamburguesa",
                "Producto test",
                new BigDecimal("5000"),
                10,
                "https://misitio.com/img.jpg",
                true,
                1L
        );

        Producto existente = new Producto();
        existente.setNombre("Hamburguesa");

        when(productoRepository.findByNombreIgnoreCaseAndEliminadoFalse("Hamburguesa"))
                .thenReturn(Optional.of(existente));

        // =========================
        // 2. Validar excepción
        // =========================
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> productoService.guardarProducto(dto)
        );

        assertEquals("El producto ya existe con nombre: Hamburguesa", exception.getMessage());
    }

    @Test
    void obtenerProductoInexistente() {

        // =========================
        // 1. Mock vacío
        // =========================
        when(productoRepository.findByIdAndEliminadoFalse(99L))
                .thenReturn(Optional.empty());

        // =========================
        // 2. Validar excepción
        // =========================
        assertThrows(
                ResourceNotFoundException.class,
                () -> productoService.obtenerPorId(99L)
        );
    }

    @Test
    void listarProductosExitoso() {

        // =========================
        // 1. Crear producto mock
        // =========================
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Pizza");

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        producto.setCategoria(categoria);

        when(productoRepository.findAllByEliminadoFalse())
                .thenReturn(List.of(producto));

        // =========================
        // 2. Ejecutar método
        // =========================
        List<ProductoResponseDTO> lista = productoService.listarProductos();

        // =========================
        // 3. Validar resultados
        // =========================
        assertEquals(1, lista.size());
        assertEquals("Pizza", lista.get(0).nombre());
    }

    @Test
    void eliminarProductoSoftDelete() {

        // =========================
        // 1. Crear producto mock
        // =========================
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setEliminado(false);

        when(productoRepository.findByIdAndEliminadoFalse(1L))
                .thenReturn(Optional.of(producto));

        // =========================
        // 2. Ejecutar método
        // =========================
        productoService.eliminarProducto(1L);

        // =========================
        // 3. Validar soft delete
        // =========================
        assertTrue(producto.getEliminado());
        verify(productoRepository).save(producto);
    }
}