package tutorgo.com.tutorgo.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        String mensaje,
        LocalDateTime timestamp
) {
    public static ErrorResponse of(String mensaje) {
        return new ErrorResponse(mensaje, LocalDateTime.now());
    }
}
