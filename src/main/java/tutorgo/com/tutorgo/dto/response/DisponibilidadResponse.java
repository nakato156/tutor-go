package tutorgo.com.tutorgo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponibilidadResponse {
    
    private Integer id;
    private LocalDate fecha;
    private LocalDateTime horaInicial;
    private LocalDateTime horaFinal;
    private Integer tutorId;
    private String tutorNombre;
    private boolean disponible;
}
