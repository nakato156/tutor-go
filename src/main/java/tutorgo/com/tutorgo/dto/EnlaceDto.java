package tutorgo.com.tutorgo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

public class EnlaceDto {
    @NotBlank(message = "El nombre del enlace no puede estar vacío.")
    @Size(max = 100, message = "El nombre del enlace no puede exceder los 100 caracteres.")
    private String nombre;

    @NotBlank(message = "La URL del enlace no puede estar vacía.")
    @URL(message = "Debe proporcionar una URL válida.")
    @Size(max = 500, message = "La URL no puede exceder los 500 caracteres.")
    private String enlace;

    // --- GETTERS Y SETTERS ---

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    // Constructor por defecto (buena práctica)
    public EnlaceDto() {
    }

    // Constructor con parámetros (opcional, pero útil)
    public EnlaceDto(String nombre, String enlace) {
        this.nombre = nombre;
        this.enlace = enlace;
    }
}