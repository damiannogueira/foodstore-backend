package foodstore_backend.service;

import foodstore_backend.dto.CategoriaResponseDTO;
import foodstore_backend.dto.ProductoCreateDTO;
import foodstore_backend.dto.ProductoEditDTO;
import foodstore_backend.dto.ProductoResponseDTO;
import foodstore_backend.exception.DuplicateResourceException;
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
        return productoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<ProductoResponseDTO> listarProductosDisponibles() {
        return productoRepository.findByDisponibleTrueAndEliminadoFalse()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ProductoResponseDTO obtenerPorId(Long id) {
        return toResponseDTO(buscarPorId(id));
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findByIdOrThrow(id, "Producto");
    }

    public ProductoResponseDTO guardarProducto(ProductoCreateDTO dto) {

        productoRepository.findByNombreIgnoreCaseAndEliminadoFalse(dto.getNombre().trim())
                .ifPresent(p -> {
                    throw new DuplicateResourceException(
                            "El producto ya existe con nombre: " + dto.getNombre()
                    );
                });

        validarImagen(dto.getImagen());

        Categoria categoria = categoriaService.buscarPorId(dto.getCategoriaId());

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre().trim());
        producto.setDescripcion(dto.getDescripcion().trim());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setImagen(dto.getImagen().trim());
        producto.setDisponible(dto.getDisponible());
        producto.setCategoria(categoria);
        producto.setEliminado(false);

        Producto productoGuardado = productoRepository.save(producto);
        return toResponseDTO(productoGuardado);
    }

    public ProductoResponseDTO actualizarProducto(Long id, ProductoEditDTO dto) {
        Producto producto = buscarPorId(id);

        if (dto.getNombre() != null) {
            String nuevoNombre = dto.getNombre().trim();

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

        if (dto.getDescripcion() != null) {
            producto.setDescripcion(dto.getDescripcion().trim());
        }

        if (dto.getPrecio() != null) {
            producto.setPrecio(dto.getPrecio());
        }

        if (dto.getStock() != null) {
            producto.setStock(dto.getStock());
        }

        if (dto.getImagen() != null) {
            validarImagen(dto.getImagen());
            producto.setImagen(dto.getImagen().trim());
        }

        if (dto.getDisponible() != null) {
            producto.setDisponible(dto.getDisponible());
        }

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaService.buscarPorId(dto.getCategoriaId());
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

    public List<ProductoResponseDTO> listarProductosPorCategoria(Long categoriaId) {
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