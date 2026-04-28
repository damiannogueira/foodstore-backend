package foodstore_backend.dto;

import foodstore_backend.model.enums.FormaPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

// DTO para crear un pedido
public record PedidoCreateDTO(

        @NotNull(message = "El usuario es obligatorio")
        Long idUsuario,

        @NotNull(message = "La forma de pago es obligatoria")
        FormaPago formaPago,

        @NotBlank(message = "El teléfono es obligatorio")
        @Size(max = 30, message = "El teléfono no puede exceder 30 caracteres")
        String telefono,

        @NotBlank(message = "La dirección de entrega es obligatoria")
        @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
        String direccionEntrega,

        // Campo opcional para observaciones del cliente (ej: sin cebolla o sin mayonesa, llamar al llegar, etc.)
        @Size(max = 500, message = "Las notas no pueden exceder 500 caracteres")
        String notas,

        @NotEmpty(message = "El pedido debe tener al menos un detalle")
        @Valid
        List<PedidoDetalleCreateDTO> detalles

) {
}

