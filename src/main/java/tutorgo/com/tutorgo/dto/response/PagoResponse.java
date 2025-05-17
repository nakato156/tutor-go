package tutorgo.com.tutorgo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponse {
    private Integer pagoId;
    private Integer sesionId;
    private Boolean exito;
    private String mensaje;
    private String estadoSesion;
}
