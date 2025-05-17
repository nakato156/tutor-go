// src/main/java/tutormaster/com/tutorgo/controller/ResenaController.java
package tutorgo.com.tutorgo.controller;

import tutorgo.com.tutorgo.dto.request.ResenaRequestDto;
import tutorgo.com.tutorgo.dto.response.ResenaResponseDto;
import tutorgo.com.tutorgo.service.ResenaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/sesiones/{sesionId}/resenas")
// @RequiredArgsConstructor // Comentado o eliminado
public class ResenaController {

    private final ResenaService resenaService;

    // Constructor explícito para inyección de dependencias
    @Autowired // Opcional si es el único constructor en versiones recientes de Spring
    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @PostMapping
    public ResponseEntity<?> crearResena(
            @PathVariable Integer sesionId,
            @RequestHeader("X-Usuario-Id") Integer usuarioEstudianteId, // Simulación
            @Valid @RequestBody ResenaRequestDto requestDto) {

        ResenaResponseDto resenaGuardada = resenaService.crearResena(sesionId, usuarioEstudianteId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Reseña enviada con éxito.", "resena", resenaGuardada));
    }
}