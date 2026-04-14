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
        return productoRepository.findAllByEliminadoFalse()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Devuelve solo los productos disponibles
    public List<ProductoResponseDTO> listarDisponibles() {
        return productoRepository.findByDisponibleTrueAndEliminadoFalse()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ProductoResponseDTO obtenerPorId(Long id) {
        return toResponseDTO(buscarPorId(id));
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + id
                ));
    }

    public ProductoResponseDTO guardarProducto(ProductoCreateDTO dto) {

        productoRepository.findByNombreIgnoreCaseAndEliminadoFalse(dto.nombre().trim())
                .ifPresent(p -> {
                    throw new DuplicateResourceException(
                            "El producto ya existe con nombre: " + dto.nombre()
                    );
                });

        validarImagen(dto.imagen());

        Categoria categoria = categoriaService.buscarPorId(dto.categoriaId());

        Producto producto = new Producto();
        producto.setNombre(dto.nombre().trim());
        producto.setDescripcion(dto.descripcion().trim());
        producto.setPrecio(dto.precio());
        producto.setStock(dto.stock());
        producto.setImagen(dto.imagen().trim());
        producto.setDisponible(dto.disponible());
        producto.setCategoria(categoria);
        producto.setEliminado(false);

        Producto productoGuardado = productoRepository.save(producto);
        return toResponseDTO(productoGuardado);
    }

    public ProductoResponseDTO actualizarProducto(Long id, ProductoEditDTO dto) {
        Producto producto = buscarPorId(id);

        if (dto.nombre() != null) {
            String nuevoNombre = dto.nombre().trim();

            if (!nuevoNombre.equalsIgnoreCase(producto.getNombre())) {
                productoRepository.findByNombreIgnoreCaseAndEliminadoFalse(nuevoNombre)
                        .ifPresent(p -> {
                            throw new DuplicateResourceException(
                                    "El producto ya existe con nombre: " + nuevoNombre
                            );
                        });
            }

            producto.setNombre(nuevoNombre);
        }

        if (dto.descripcion() != null) {
            producto.setDescripcion(dto.descripcion().trim());
        }

        if (dto.precio() != null) {
            producto.setPrecio(dto.precio());
        }

        if (dto.stock() != null) {
            producto.setStock(dto.stock());
        }

        if (dto.imagen() != null) {
            validarImagen(dto.imagen());
            producto.setImagen(dto.imagen().trim());
        }

        if (dto.disponible() != null) {
            producto.setDisponible(dto.disponible());
        }

        if (dto.categoriaId() != null) {
            Categoria categoria = categoriaService.buscarPorId(dto.categoriaId());
            producto.setCategoria(categoria);
        }

        Producto productoGuardado = productoRepository.save(producto);
        return toResponseDTO(productoGuardado);
    }

    public void eliminarProducto(Long id) {
        Producto producto = buscarPorId(id);
        producto.setEliminado(true);
        productoRepository.save(producto);
    }

    // Devuelve los productos de una categoría
    public List<ProductoResponseDTO> listarPorCategoria(Long categoriaId) {
        categoriaService.buscarPorId(categoriaId);

        return productoRepository.findByCategoriaIdAndEliminadoFalse(categoriaId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private void validarImagen(String imagen) {
        String valor = imagen == null ? "" : imagen.trim().toLowerCase();

        boolean esValida = valor.startsWith("http://") || valor.startsWith("https://");

        if (!esValida) {
            throw new IllegalArgumentException("La imagen debe ser una URL válida");
        }
    }

    private ProductoResponseDTO toResponseDTO(Producto producto) {
        Categoria categoria = producto.getCategoria();

        CategoriaResponseDTO categoriaDTO = new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getImagen()
        );

        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getImagen(),
                producto.getDisponible(),
                categoriaDTO
        );
    }
}