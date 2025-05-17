package tutorgo.com.tutorgo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(
        @NotBlank(message = "La contraseña actual es obligatoria")
        String currentPassword,

        @NotBlank(message = "La nueva contraseña es obligatoria")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$",
                message = "La nueva contraseña debe tener al menos 8 caracteres, una mayúscula y un número"
        )
        String newPassword,

        @NotBlank(message = "La confirmación de la nueva contraseña es obligatoria")
        String confirmPassword
) {
}
