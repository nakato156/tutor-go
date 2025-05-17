package tutorgo.com.tutorgo.dto.response;

import java.util.List;

public record TutorListResponse(
        List<TutorResponse> tutores,
        int paginaActual,
        int totalPaginas,
        long totalElementos,
        boolean isEmpty
) {
    // Método auxiliar para verificar si está vacío
    public boolean isEmpty() {
        return isEmpty;
    }
    
    // Método estático para crear una instancia vacía
    public static TutorListResponse emptyList() {
        return new TutorListResponse(
                List.of(),
                0,
                0,
                0,
                true
        );
    }
}
