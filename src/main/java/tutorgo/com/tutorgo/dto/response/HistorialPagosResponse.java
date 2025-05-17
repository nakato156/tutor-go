package tutorgo.com.tutorgo.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistorialPagosResponse {
    private Integer usuarioId;
    private List<TransaccionResponse> transacciones;
    private Boolean exito;
    private String mensaje;
}
