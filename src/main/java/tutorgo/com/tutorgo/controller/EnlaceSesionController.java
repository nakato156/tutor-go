package tutorgo.com.tutorgo.controller;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.expression.ExpressionException;
import tutorgo.com.tutorgo.dto.request.AdjuntarEnlacesRequestDto;
import tutorgo.com.tutorgo.dto.EnlaceDto;
import tutorgo.com.tutorgo.exception.AccessDeniedException;
import tutorgo.com.tutorgo.exception.BadRequestException;
import tutorgo.com.tutorgo.model.entity.Sesion;
import tutorgo.com.tutorgo.model.entity.Tutor;
import tutorgo.com.tutorgo.repository.SesionRepository;
import tutorgo.com.tutorgo.service.EnlaceSesionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sesiones/{sesionId}/enlaces")
public class EnlaceSesionController {

    private final EnlaceSesionService enlaceSesionService;
    private final SesionRepository sesionRepository;

    @Autowired
    public EnlaceSesionController(EnlaceSesionService enlaceSesionService, SesionRepository sesionRepository) {
        this.enlaceSesionService = enlaceSesionService;
        this.sesionRepository = sesionRepository;
    }

    @PutMapping
    public ResponseEntity<?> adjuntarEnlaces(
            @PathVariable Integer sesionId,
            @RequestHeader("X-Usuario-Id") Integer idTutorSolicitante,
            @Valid @RequestBody AdjuntarEnlacesRequestDto requestDto) {
        Sesion s = sesionRepository.findById(sesionId)
                .orElseThrow(() -> new BadRequestException("No se encontro la sesion"));
        Integer ti = s.getTutor().getId();
        if (!s.getTutor().getId().equals(idTutorSolicitante)) {
            throw new AccessDeniedException("No eres el tutor de esta sesión");
        }
        List<EnlaceDto> enlacesGuardados = enlaceSesionService.adjuntarEnlaces(sesionId, ti, requestDto);
        return ResponseEntity.ok(Map.of("message", "Enlaces guardados.", "enlaces", enlacesGuardados));
    }
}