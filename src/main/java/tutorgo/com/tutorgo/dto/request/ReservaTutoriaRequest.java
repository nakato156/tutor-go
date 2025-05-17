package tutorgo.com.tutorgo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaTutoriaRequest {
    
    @NotNull(message = "El ID de la disponibilidad es obligatorio")
    private Integer disponibilidadId;
    
    @NotNull(message = "El ID del estudiante es obligatorio")
    private Integer estudianteId;
    
    // No es necesario el ID del tutor, ya que se obtiene de la disponibilidad
}
