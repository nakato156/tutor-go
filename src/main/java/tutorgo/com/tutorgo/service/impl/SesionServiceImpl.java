package tutorgo.com.tutorgo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tutorgo.com.tutorgo.model.entity.EnlaceSesion;
import tutorgo.com.tutorgo.model.entity.Sesion;
import tutorgo.com.tutorgo.repository.EnlaceSesionRepository;
import tutorgo.com.tutorgo.repository.SesionRepository;
import tutorgo.com.tutorgo.service.SesionService;

@Service
@RequiredArgsConstructor
public class SesionServiceImpl implements SesionService {

    private final SesionRepository sesionRepository;               // <--- Inyectamos este repo
    private final EnlaceSesionRepository enlaceSesionRepository;   // <--- Y este también

    @Override
    public String obtenerEnlaceSesion(Integer sesionId, Integer estudianteId) {
        // Validamos que la sesión exista y que pertenezca al estudiante
        Sesion sesion = sesionRepository.findByIdAndEstudianteId(sesionId, estudianteId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada o no pertenece al estudiante."));

        // Obtenemos el enlace asociado a esa sesión
        EnlaceSesion enlaceSesion = enlaceSesionRepository.findBySesionId(sesionId)
                .orElseThrow(() -> new RuntimeException("No se encontró el enlace para esta sesión."));

        // Retornamos el enlace
        return enlaceSesion.getEnlace();
    }
}
