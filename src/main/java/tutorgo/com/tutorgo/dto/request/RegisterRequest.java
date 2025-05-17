package tutorgo.com.tutorgo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
        String nombre,

        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "Formato de email inválido")
        @Size(max = 150, message = "El email no puede exceder los 150 caracteres")
        String email,

        @NotBlank(message = "La contraseña no puede estar vacía")
        @Size(min = 6, max = 150, message = "La contraseña debe tener entre 6 y 150 caracteres")
        String password,

        Integer rolId
) {}