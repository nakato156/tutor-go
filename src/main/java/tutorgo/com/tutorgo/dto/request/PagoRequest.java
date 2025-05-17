package tutorgo.com.tutorgo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tutorgo.com.tutorgo.model.enums.MetodoPago;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoRequest {
    
    @NotNull(message = "La sesión es obligatoria")
    private Integer sesionId;
    
    @NotNull(message = "El estudiante es obligatorio")
    private Integer estudianteId;

    @NotNull(message = "El método de pago es obligatorio")
    private String metodoPago;
    
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    private Double monto;
    
    @NotNull(message = "El número de tarjeta es obligatorio")
    @Size(min = 16, max = 16, message = "El número de tarjeta debe tener 16 dígitos")
    private String numeroTarjeta;
    
    @NotNull(message = "La fecha de expiración es obligatoria")
    @Size(min = 5, max = 5, message = "La fecha de expiración debe tener formato MM/YY")
    private String fechaExpiracion;
    
    @NotNull(message = "El código de seguridad es obligatorio")
    @Size(min = 3, max = 4, message = "El código de seguridad debe tener 3 o 4 dígitos")
    private String codigoSeguridad;

}
