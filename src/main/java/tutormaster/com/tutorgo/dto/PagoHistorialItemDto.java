package tutormaster.com.tutorgo.dto;

import tutormaster.com.tutorgo.model.enums.EstadoPago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoHistorialItemDto {
    private Integer pagoId;
    private BigDecimal monto;
    private String descripcion;
    private EstadoPago estado;
    private String tipo;
    private String contraparteNombre;
}