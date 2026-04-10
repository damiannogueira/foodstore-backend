package foodstore_backend.dto;

import foodstore_backend.model.enums.FormaPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

// DTO para crear un pedido
public record PedidoCreateDTO(

        @NotNull(message = "El usuario es obligatorio")
        Long usuarioId,

        @NotNull(message = "La forma de pago es obligatoria")
        FormaPago formaPago,

        @NotNull(message = "El teléfono es obligatorio")
        String telefono,

        @NotNull(message = "La dirección es obligatoria")
        String direccionEntrega,

        @Size(max = 500)
        String notas,

        @Valid
        @NotNull(message = "Los detalles son obligatorios")
        List<PedidoDetalleCreateDTO> detalles

) {
}

