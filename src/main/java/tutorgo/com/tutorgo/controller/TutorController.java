package tutorgo.com.tutorgo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tutorgo.com.tutorgo.dto.response.ErrorResponse;
import tutorgo.com.tutorgo.dto.response.TutorListResponse;
import tutorgo.com.tutorgo.service.TutorService;

@RestController
@RequestMapping("/tutores")
public class TutorController {

    private final TutorService tutorService;

    @Autowired
    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @GetMapping
    public ResponseEntity<?> listarTutores(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanio) {
        try {
            TutorListResponse response = tutorService.obtenerTutores(pagina, tamanio);
            
            if (response.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ErrorResponse.of("Aún no hay tutores disponibles. Vuelve a intentarlo más tarde."));
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.of("Ocurrió un error al cargar los tutores. Intenta de nuevo más tarde."));
        }
    }
}
