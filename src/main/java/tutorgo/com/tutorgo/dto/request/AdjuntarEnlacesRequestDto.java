package tutorgo.com.tutorgo.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tutorgo.com.tutorgo.dto.EnlaceDto;

import java.util.List;


public class AdjuntarEnlacesRequestDto {
    @NotNull(message = "La lista de enlaces no puede ser nula.")
    @Size(max = 5, message = "Se pueden adjuntar hasta 5 enlaces.")
    private List<@Valid EnlaceDto> enlaces;

    // --- GETTERS Y SETTERS ---

    public List<EnlaceDto> getEnlaces() {
        return enlaces;
    }

    public void setEnlaces(List<EnlaceDto> enlaces) {
        this.enlaces = enlaces;
    }

    // Constructor por defecto (buena práctica)
    public AdjuntarEnlacesRequestDto() {
    }

    // Constructor con parámetros (opcional)
    public AdjuntarEnlacesRequestDto(List<EnlaceDto> enlaces) {
        this.enlaces = enlaces;
    }
}