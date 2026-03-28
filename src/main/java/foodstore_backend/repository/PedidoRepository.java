package foodstore_backend.repository;

import foodstore_backend.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio para acceder a los datos de pedidos
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Devuelve todos los pedidos no eliminados
    List<Pedido> findByEliminadoFalse();

    // Devuelve los pedidos no eliminados de un usuario
    List<Pedido> findByUsuarioIdAndEliminadoFalse(Long usuarioId);
}