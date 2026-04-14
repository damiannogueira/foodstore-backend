package foodstore_backend.service;

import foodstore_backend.dto.CategoriaCreateDTO;
import foodstore_backend.dto.CategoriaEditDTO;
import foodstore_backend.dto.CategoriaResponseDTO;
import foodstore_backend.exception.DuplicateResourceException;
import foodstore_backend.exception.ResourceNotFoundException;
import foodstore_backend.model.Categoria;
import foodstore_backend.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Tests unitarios para CategoriaService
@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void guardarCategoriaExitoso() {
        CategoriaCreateDTO dto = new CategoriaCreateDTO(
                "Hamburguesas",
                "Categoría de hamburguesas",
                "https://misitio.com/hamburguesas.jpg"
        );

        Categoria categoriaGuardada = new Categoria();
        categoriaGuardada.setId(1L);
        categoriaGuardada.setNombre("Hamburguesas");
        categoriaGuardada.setDescripcion("Categoría de hamburguesas");
        categoriaGuardada.setImagen("https://misitio.com/hamburguesas.jpg");
        categoriaGuardada.setEliminado(false);

        when(categoriaRepository.findByNombreIgnoreCaseAndEliminadoFalse("Hamburguesas"))
                .thenReturn(Optional.empty());
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaGuardada);

        CategoriaResponseDTO response = categoriaService.guardarCategoria(dto);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Hamburguesas", response.nombre());
        assertEquals("Categoría de hamburguesas", response.descripcion());
        assertEquals("https://misitio.com/hamburguesas.jpg", response.imagen());
    }

    @Test
    void guardarCategoriaDuplicada() {
        CategoriaCreateDTO dto = new CategoriaCreateDTO(
                "Hamburguesas",
                "Categoría de hamburguesas",
                "https://misitio.com/hamburguesas.jpg"
        );

        Categoria categoriaExistente = new Categoria();
        categoriaExistente.setId(1L);
        categoriaExistente.setNombre("Hamburguesas");

        when(categoriaRepository.findByNombreIgnoreCaseAndEliminadoFalse("Hamburguesas"))
                .thenReturn(Optional.of(categoriaExistente));

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> categoriaService.guardarCategoria(dto)
        );

        assertEquals("La categoría ya existe con nombre: Hamburguesas", exception.getMessage());
    }

    @Test
    void obtenerCategoriaPorIdExitoso() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Pizzas");
        categoria.setDescripcion("Categoría de pizzas");
        categoria.setImagen("https://misitio.com/pizzas.jpg");
        categoria.setEliminado(false);

        when(categoriaRepository.findByIdAndEliminadoFalse(1L))
                .thenReturn(Optional.of(categoria));

        CategoriaResponseDTO response = categoriaService.obtenerPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Pizzas", response.nombre());
    }

    @Test
    void obtenerCategoriaPorIdInexistente() {
        when(categoriaRepository.findByIdAndEliminadoFalse(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoriaService.obtenerPorId(99L)
        );

        assertEquals("Categoría no encontrada con id: 99", exception.getMessage());
    }

    @Test
    void actualizarCategoriaExitoso() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Pizzas");
        categoria.setDescripcion("Categoría original");
        categoria.setImagen("https://misitio.com/pizzas.jpg");
        categoria.setEliminado(false);

        CategoriaEditDTO dto = new CategoriaEditDTO(
                "Empanadas",
                "Nueva descripción",
                "https://misitio.com/empanadas.jpg"
        );

        when(categoriaRepository.findByIdAndEliminadoFalse(1L))
                .thenReturn(Optional.of(categoria));
        when(categoriaRepository.findByNombreIgnoreCaseAndEliminadoFalse("Empanadas"))
                .thenReturn(Optional.empty());
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaResponseDTO response = categoriaService.actualizarCategoria(1L, dto);

        assertNotNull(response);
        assertEquals("Empanadas", response.nombre());
        assertEquals("Nueva descripción", response.descripcion());
        assertEquals("https://misitio.com/empanadas.jpg", response.imagen());
    }

    @Test
    void listarCategoriasSoloActivas() {
        Categoria c1 = new Categoria();
        c1.setId(1L);
        c1.setNombre("Pizzas");
        c1.setDescripcion("Desc 1");
        c1.setImagen("https://misitio.com/pizzas.jpg");

        Categoria c2 = new Categoria();
        c2.setId(2L);
        c2.setNombre("Hamburguesas");
        c2.setDescripcion("Desc 2");
        c2.setImagen("https://misitio.com/hamburguesas.jpg");

        when(categoriaRepository.findAllByEliminadoFalse())
                .thenReturn(List.of(c1, c2));

        List<CategoriaResponseDTO> response = categoriaService.listarCategorias();

        assertEquals(2, response.size());
        assertEquals("Pizzas", response.get(0).nombre());
        assertEquals("Hamburguesas", response.get(1).nombre());
    }

    @Test
    void eliminarCategoriaSoftDelete() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Pizzas");
        categoria.setEliminado(false);

        when(categoriaRepository.findByIdAndEliminadoFalse(1L))
                .thenReturn(Optional.of(categoria));

        categoriaService.eliminarCategoria(1L);

        assertEquals(true, categoria.getEliminado());
        verify(categoriaRepository).save(categoria);
    }
}