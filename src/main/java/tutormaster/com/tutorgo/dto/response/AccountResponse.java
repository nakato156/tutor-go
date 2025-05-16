package tutormaster.com.tutorgo.dto.response;

import java.time.LocalDateTime;

public record AccountResponse(
    Long id,
    String name,
    String email,
    String role,
    LocalDateTime createdAt,
    String phoneNumber
) {}