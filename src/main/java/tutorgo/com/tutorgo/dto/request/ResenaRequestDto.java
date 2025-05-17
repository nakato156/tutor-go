package tutorgo.com.tutorgo.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


public class ResenaRequestDto {
    @NotNull(message = "La calificación es obligatoria.")
    @Min(value = 1, message = "La calificación debe ser entre 1 y 5.")
    @Max(value = 5, message = "La calificación debe ser entre 1 y 5.")
    private Integer calificacion;

    @Size(max = 500, message = "El comentario no puede exceder los 500 caracteres.")
    private String comentario;

    // --- GETTERS Y SETTERS ---

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    // Constructor por defecto (generalmente bueno tenerlo para DTOs)
    public ResenaRequestDto() {
    }

    // Constructor con parámetros (opcional, pero puede ser útil)
    public ResenaRequestDto(Integer calificacion, String comentario) {
        this.calificacion = calificacion;
        this.comentario = comentario;
    }
}
