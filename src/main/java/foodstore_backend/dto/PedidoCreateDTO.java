package foodstore_backend.dto;

import foodstore_backend.model.enums.FormaPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

// DTO para crear un pedido
public record PedidoCreateDTO(

        @NotNull(message = "El usuario es obligatorio")
        Long idUsuario,

        @NotNull(message = "La forma de pago es obligatoria")
        FormaPago formaPago,

        @NotBlank(message = "El teléfono es obligatorio")
        String telefono,

        @NotBlank(message = "La dirección de entrega es obligatoria")
        String direccionEntrega,

        // Campo opcional para observaciones del cliente (ej: sin cebolla o sin mayonesa, llamar al llegar, etc.)
        String notas,

        @NotEmpty(message = "El pedido debe tener al menos un detalle")
        @Valid
        List<PedidoDetalleCreateDTO> detalles

) {
}

