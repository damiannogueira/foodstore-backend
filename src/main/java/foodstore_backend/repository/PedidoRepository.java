package foodstore_backend.repository;

import foodstore_backend.model.Pedido;
import foodstore_backend.model.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Optional<Pedido> findByIdAndEliminadoFalse(Long id);

    List<Pedido> findByEliminadoFalseOrderByFechaDesc();

    List<Pedido> findByUsuarioIdAndEliminadoFalseOrderByFechaDesc(Long usuarioId);

    List<Pedido> findByEstadoAndEliminadoFalseOrderByFechaDesc(EstadoPedido estado);
}