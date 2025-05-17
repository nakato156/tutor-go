package tutorgo.com.tutorgo.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransaccionResponse {
    private Integer id;
    private LocalDateTime fecha;
    private BigDecimal monto;
    private String descripcion;
    private String estado;
    private String tipo;  // "INGRESO" o "PAGO"
    private String conceptoReferencia;  // Referencia a la sesión o concepto relacionado
}
