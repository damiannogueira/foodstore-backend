package foodstore_backend.config;

import foodstore_backend.model.Categoria;
import foodstore_backend.model.Producto;
import foodstore_backend.repository.CategoriaRepository;
import foodstore_backend.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

// Carga datos iniciales de categorías y productos si la base está vacía
@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public DataSeeder(CategoriaRepository categoriaRepository,
                      ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) {

        // Evita duplicar productos cada vez que inicia la aplicación
        if (productoRepository.findByNombreIgnoreCaseAndEliminadoFalse("Pizza Muzzarella").isPresent()) {
            return;
        }

        Map<String, Categoria> categorias = new HashMap<>();

        categorias.put("Pizzas", guardarCategoria("Pizzas",
                "Pizzas artesanales con masa fresca",
                "https://images.unsplash.com/photo-1513104890138-7c749659a591"));

        categorias.put("Hamburguesas", guardarCategoria("Hamburguesas",
                "Hamburguesas gourmet con ingredientes frescos",
                "https://images.unsplash.com/photo-1568901346375-23c9450c58cd"));

        categorias.put("Bebidas", guardarCategoria("Bebidas",
                "Gaseosas, jugos y bebidas frías",
                "https://images.unsplash.com/photo-1544145945-f90425340c7e"));

        categorias.put("Postres", guardarCategoria("Postres",
                "Tortas, helados y dulces artesanales",
                "https://images.unsplash.com/photo-1551024506-0bccd828d307"));

        categorias.put("Empanadas", guardarCategoria("Empanadas",
                "Empanadas horneadas y fritas de distintos sabores",
                "https://images.unsplash.com/photo-1601050690597-df0568f70950"));

        categorias.put("Ensaladas", guardarCategoria("Ensaladas",
                "Ensaladas frescas y saludables",
                "https://images.unsplash.com/photo-1512621776951-a57141f2eefd"));

        guardarProducto("Pizza Muzzarella", "Pizza clásica con salsa de tomate y muzzarella derretida",
                "4500.00", 20, true, categorias.get("Pizzas"));

        guardarProducto("Pizza Napolitana", "Pizza con rodajas de tomate fresco, ajo y albahaca",
                "5200.00", 15, true, categorias.get("Pizzas"));

        guardarProducto("Pizza Especial 4 Quesos", "Muzzarella, provolone, roquefort y parmesano",
                "6800.00", 10, true, categorias.get("Pizzas"));

        guardarProducto("Hamburguesa Clásica", "Medallón de carne, lechuga, tomate, cebolla y mayo",
                "3800.00", 30, true, categorias.get("Hamburguesas"));

        guardarProducto("Hamburguesa BBQ Bacon", "Doble medallón, bacon crocante y salsa barbacoa ahumada",
                "5100.00", 25, true, categorias.get("Hamburguesas"));

        guardarProducto("Hamburguesa Veggie", "Medallón de lentejas y garbanzo, cheddar vegano y rúcula",
                "4200.00", 0, false, categorias.get("Hamburguesas"));

        guardarProducto("Coca-Cola 500ml", "Gaseosa Coca-Cola fría, botella personal",
                "1200.00", 100, true, categorias.get("Bebidas"));

        guardarProducto("Jugo de Naranja Natural", "Jugo exprimido en el momento, vaso 400ml",
                "1800.00", 40, true, categorias.get("Bebidas"));

        guardarProducto("Agua Mineral 500ml", "Agua mineral sin gas, botella personal",
                "800.00", 150, true, categorias.get("Bebidas"));

        guardarProducto("Torta Rogel", "Torta rogel tradicional con dulce de leche y merengue",
                "3500.00", 12, true, categorias.get("Postres"));

        guardarProducto("Helado Artesanal 2 gustos", "Pote de 250g, elegí 2 gustos entre 12 opciones",
                "2800.00", 30, true, categorias.get("Postres"));

        guardarProducto("Brownie con Helado", "Brownie de chocolate tibio con bocha de vainilla",
                "2200.00", 0, false, categorias.get("Postres"));

        guardarProducto("Empanadas de Carne x6", "Empanadas criollas de carne cortada a cuchillo, horneadas",
                "3000.00", 50, true, categorias.get("Empanadas"));

        guardarProducto("Empanadas de Pollo x6", "Empanadas de pollo con morrón y verdeo, horneadas",
                "2800.00", 45, true, categorias.get("Empanadas"));

        guardarProducto("Empanadas de Jamón y Queso x6", "Empanadas fritas con jamón cocido y queso fundido",
                "2500.00", 60, true, categorias.get("Empanadas"));

        guardarProducto("Ensalada César", "Lechuga romana, crutones, parmesano y aderezo césar",
                "3200.00", 20, true, categorias.get("Ensaladas"));

        guardarProducto("Ensalada Caprese", "Tomate, muzzarella fresca, albahaca y aceite de oliva",
                "2900.00", 18, true, categorias.get("Ensaladas"));

        guardarProducto("Ensalada Mixta", "Lechuga, tomate, zanahoria rallada y aceitunas",
                "2400.00", 25, true, categorias.get("Ensaladas"));

        System.out.println("Datos iniciales de Food Store cargados correctamente.");
    }

    private Categoria guardarCategoria(String nombre, String descripcion, String imagen) {
        return categoriaRepository.findByNombreIgnoreCaseAndEliminadoFalse(nombre)
                .orElseGet(() -> categoriaRepository.save(new Categoria(nombre, descripcion, imagen)));
    }

    private void guardarProducto(String nombre, String descripcion, String precio,
                                 Integer stock, Boolean disponible, Categoria categoria) {

        if (productoRepository.findByNombreIgnoreCaseAndEliminadoFalse(nombre).isPresent()) {
            return;
        }

        Producto producto = new Producto(
                nombre,
                descripcion,
                new BigDecimal(precio),
                stock,
                categoria.getImagen(),
                disponible,
                categoria
        );

        productoRepository.save(producto);
    }
}
