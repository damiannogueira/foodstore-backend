package foodstore_backend.repository;

import foodstore_backend.model.Pedido;
import foodstore_backend.model.enums.EstadoPedido;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio para acceder a los datos de pedidos
@Repository
public interface PedidoRepository extends BaseRepository<Pedido, Long> {

    List<Pedido> findByEliminadoFalse();

    List<Pedido> findByUsuarioIdAndEliminadoFalse(Long usuarioId);

    List<Pedido> findByEstadoAndEliminadoFalse(EstadoPedido estado);
}