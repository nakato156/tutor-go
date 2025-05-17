package tutorgo.com.tutorgo.dto.response;

import tutorgo.com.tutorgo.model.entity.Rol;

public record LoginResponse(
        Integer id,
        String nombre,
        String email,
        String token,
        String rolNombre
) {
}
