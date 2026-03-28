package foodstore_backend.service;

import foodstore_backend.dto.CategoriaResponseDTO;
import foodstore_backend.dto.ProductoCreateDTO;
import foodstore_backend.dto.ProductoEditDTO;
import foodstore_backend.dto.ProductoResponseDTO;
import foodstore_backend.exception.DuplicateResourceException;
import foodstore_backend.exception.ResourceNotFoundException;
import foodstore_backend.model.Categoria;
import foodstore_backend.model.Producto;
import foodstore_backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Servicio que contiene la lógica de negocio para manejar productos
@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaService categoriaService;

    public List<ProductoResponseDTO> listarProductos() {
        return productoRepository.findByEliminadoFalse()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ProductoResponseDTO obtenerPorId(Long id) {
        return toResponseDTO(buscarPorId(id));
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    // Guarda un producto validando nombre duplicado y existencia de categoría
    public ProductoResponseDTO guardarProducto(ProductoCreateDTO productoCreateDTO) {

        productoRepository.findByNombre(productoCreateDTO.getNombre())
                .ifPresent(p -> {
                    throw new DuplicateResourceException(
                            "El producto ya existe con nombre: " + productoCreateDTO.getNombre()
                    );
                });

        Long categoriaId = productoCreateDTO.getCategoriaId();
        Categoria categoria = categoriaService.buscarPorId(categoriaId);

        Producto producto = new Producto();
        producto.setNombre(productoCreateDTO.getNombre());
        producto.setDescripcion(productoCreateDTO.getDescripcion());
        producto.setPrecio(productoCreateDTO.getPrecio());
        producto.setStock(productoCreateDTO.getStock());
        producto.setCategoria(categoria);
        producto.setEliminado(false);

        Producto productoGuardado = productoRepository.save(producto);
        return toResponseDTO(productoGuardado);
    }

    // Realiza baja lógica sin eliminar físicamente el usuario
    public ProductoResponseDTO actualizarProducto(Long id, ProductoEditDTO productoEditDTO) {
        Producto producto = buscarPorId(id);

        String nuevoNombre = productoEditDTO.getNombre();
        if (nuevoNombre != null && !nuevoNombre.equalsIgnoreCase(producto.getNombre())) {
            productoRepository.findByNombre(nuevoNombre)
                    .ifPresent(p -> {
                        throw new DuplicateResourceException(
                                "El producto ya existe con nombre: " + nuevoNombre
                        );
                    });
            producto.setNombre(nuevoNombre);
        }

        String nuevaDescripcion = productoEditDTO.getDescripcion();
        if (nuevaDescripcion != null) {
            producto.setDescripcion(nuevaDescripcion);
        }

        if (productoEditDTO.getPrecio() != null) {
            producto.setPrecio(productoEditDTO.getPrecio());
        }

        if (productoEditDTO.getStock() != null) {
            producto.setStock(productoEditDTO.getStock());
        }

        Long categoriaId = productoEditDTO.getCategoriaId();
        if (categoriaId != null) {
            Categoria categoria = categoriaService.buscarPorId(categoriaId);
            producto.setCategoria(categoria);
        }

        Producto productoGuardado = productoRepository.save(producto);
        return toResponseDTO(productoGuardado);
    }

    // Realiza baja lógica sin eliminar físicamente el registro
    public void eliminarProducto(Long id) {
        Producto producto = buscarPorId(id);
        producto.setEliminado(true);
        productoRepository.save(producto);
    }

    public List<ProductoResponseDTO> listarProductosPorCategoria(Long categoriaId) {
        categoriaService.buscarPorId(categoriaId);

        return productoRepository.findByCategoriaIdAndEliminadoFalse(categoriaId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private ProductoResponseDTO toResponseDTO(Producto producto) {
        Categoria categoria = producto.getCategoria();

        CategoriaResponseDTO categoriaDTO = new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );

        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                categoriaDTO
        );
    }
}