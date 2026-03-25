package foodstore_backend.repository;

import foodstore_backend.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositorio para acceder a los detalles de pedido en la base de datos
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
}