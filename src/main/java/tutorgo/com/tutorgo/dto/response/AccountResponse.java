package tutorgo.com.tutorgo.dto.response;

import java.sql.Timestamp;

public record AccountResponse(
        Integer id,
        String nombre,
        String email,
        String rolNombre,
        Timestamp fechaCreacion
) {}