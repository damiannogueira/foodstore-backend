package foodstore_backend.service;

import foodstore_backend.dto.PedidoCreateDTO;
import foodstore_backend.dto.PedidoDetalleCreateDTO;
import foodstore_backend.dto.PedidoResponseDTO;
import foodstore_backend.exception.InsufficientStockException;
import foodstore_backend.model.Pedido;
import foodstore_backend.model.Producto;
import foodstore_backend.model.Usuario;
import foodstore_backend.model.enums.FormaPago;
import foodstore_backend.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// Tests unitarios para PedidoService
@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void guardarPedidoConStockInsuficiente() {
        PedidoDetalleCreateDTO detalle = new PedidoDetalleCreateDTO(1L, 10);

        PedidoCreateDTO dto = new PedidoCreateDTO(
                1L,
                FormaPago.EFECTIVO,
                "123456789",
                "Calle 123",
                null,
                List.of(detalle)
        );

        Usuario usuario = new Usuario();

        Producto producto = new Producto();
        producto.setNombre("Hamburguesa");
        producto.setPrecio(new BigDecimal("5000"));
        producto.setStock(5);
        producto.setDisponible(true);

        when(usuarioService.buscarEntidadPorId(1L)).thenReturn(usuario);
        when(productoService.buscarPorId(1L)).thenReturn(producto);

        InsufficientStockException exception = assertThrows(
                InsufficientStockException.class,
                () -> pedidoService.guardarPedido(dto)
        );

        assertEquals("Stock insuficiente para el producto: Hamburguesa", exception.getMessage());
    }

    @Test
    void guardarPedidoExitoso() {
        PedidoDetalleCreateDTO detalle = new PedidoDetalleCreateDTO(1L, 2);

        PedidoCreateDTO dto = new PedidoCreateDTO(
                1L,
                FormaPago.EFECTIVO,
                "123456789",
                "Calle 123",
                "Sin cebolla",
                List.of(detalle)
        );

        Usuario usuario = new Usuario();
        usuario.setNombre("Damian");
        usuario.setApellido("Nogueira");

        Producto producto = new Producto();
        producto.setNombre("Hamburguesa");
        producto.setPrecio(new BigDecimal("5000"));
        producto.setStock(10);
        producto.setImagen("img.jpg");
        producto.setDisponible(true);

        when(usuarioService.buscarEntidadPorId(1L)).thenReturn(usuario);
        when(productoService.buscarPorId(1L)).thenReturn(producto);

        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PedidoResponseDTO response = pedidoService.guardarPedido(dto);

        assertNotNull(response);
        assertEquals(new BigDecimal("10000"), response.total());
        assertEquals(FormaPago.EFECTIVO, response.formaPago());
        assertEquals("123456789", response.telefono());
        assertEquals("Calle 123", response.direccionEntrega());
        assertEquals(8, producto.getStock());
        assertEquals(1, response.detalles().size());
    }
}