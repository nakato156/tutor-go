package tutorgo.com.tutorgo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SesionResponse {
    
    private Integer id;
    private LocalDateTime fecha;
    private LocalDateTime horaInicial;
    private LocalDateTime horaFinal;
    private String tipoEstado;
    private Integer tutorId;
    private String tutorNombre;
    private Integer estudianteId;
}
