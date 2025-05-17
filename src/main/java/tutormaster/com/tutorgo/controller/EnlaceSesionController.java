package tutormaster.com.tutorgo.controller;

import tutormaster.com.tutorgo.dto.request.AdjuntarEnlacesRequestDto;
import tutormaster.com.tutorgo.dto.EnlaceDto;
import tutormaster.com.tutorgo.service.EnlaceSesionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sesiones/{sesionId}/enlaces")
public class EnlaceSesionController {

    private final EnlaceSesionService enlaceSesionService;

    @Autowired
    public EnlaceSesionController(EnlaceSesionService enlaceSesionService) {
        this.enlaceSesionService = enlaceSesionService;
    }

    @PutMapping
    public ResponseEntity<?> adjuntarEnlaces(
            @PathVariable Integer sesionId,
            @RequestHeader("X-Usuario-Id") Integer usuarioTutorId, // Simulación
            @Valid @RequestBody AdjuntarEnlacesRequestDto requestDto) {

        List<EnlaceDto> enlacesGuardados = enlaceSesionService.adjuntarEnlaces(sesionId, usuarioTutorId, requestDto);
        return ResponseEntity.ok(Map.of("message", "Enlaces guardados.", "enlaces", enlacesGuardados));
    }
}