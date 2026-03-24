package foodstore_backend.service;

import foodstore_backend.exception.DuplicateResourceException;
import foodstore_backend.exception.ResourceNotFoundException;
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

    // Devuelve todos los productos
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    // Busca un producto por ID o lanza excepción si no existe
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    // Guarda un producto validando nombre duplicado y categoría existente
    public Producto guardarProducto(Producto producto) {

        productoRepository.findByNombre(producto.getNombre())
                .ifPresent(p -> {
                    throw new DuplicateResourceException("El producto ya existe");
                });

        Long categoriaId = producto.getCategoria().getId();
        producto.setCategoria(categoriaService.buscarPorId(categoriaId));

        return productoRepository.save(producto);
    }

    // Elimina un producto verificando que exista
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        productoRepository.delete(producto);
    }

    // Actualiza un producto existente
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        productoRepository.findByNombre(productoActualizado.getNombre())
                .ifPresent(p -> {
                    if (!p.getId().equals(id)) {
                        throw new DuplicateResourceException("El producto ya existe");
                    }
                });

        Long categoriaId = productoActualizado.getCategoria().getId();
        productoExistente.setCategoria(categoriaService.buscarPorId(categoriaId));

        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setDescripcion(productoActualizado.getDescripcion());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setStock(productoActualizado.getStock());

        return productoRepository.save(productoExistente);
    }
}
