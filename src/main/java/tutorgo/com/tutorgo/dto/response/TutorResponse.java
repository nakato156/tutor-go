package tutorgo.com.tutorgo.dto.response;

public record TutorResponse(
        Integer id,
        String nombre,
        String rubro,
        Double estrellasPromedio,
        String fotoUrl,
        Double tarifaHora
) {
}
