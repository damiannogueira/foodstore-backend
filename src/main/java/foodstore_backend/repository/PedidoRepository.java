package foodstore_backend.repository;

import foodstore_backend.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repositorio para acceder a pedidos en la base de datos
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Buscar pedidos por usuario
    List<Pedido> findByUsuarioId(Long usuarioId);
}