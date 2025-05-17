package tutorgo.com.tutorgo.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tutorgo.com.tutorgo.model.entity.Estudiante;
import tutorgo.com.tutorgo.model.entity.Sesion;
import tutorgo.com.tutorgo.repository.EstudianteRepository;
import tutorgo.com.tutorgo.repository.SesionRepository;
import tutorgo.com.tutorgo.service.AccountService;  // Interfaz

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final EstudianteRepository estudianteRepository;
    private final SesionRepository sesionRepository;

    @Override
    public List<Sesion> obtenerSesionesDesde2023(Integer estudianteId) {
        LocalDateTime fechaInicio = LocalDateTime.of(2023, 1, 1, 0, 0);
        return sesionRepository.findSesionesDesdeFecha(estudianteId, fechaInicio);
    }

    @Override
    public String obtenerEnlaceSesionActiva(Integer estudianteId) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        List<Sesion> sesionesFuturas = sesionRepository.findSesionesFuturasByEstudianteId(estudianteId);

        if (sesionesFuturas == null || sesionesFuturas.isEmpty()) {
            throw new IllegalStateException("No hay sesiones futuras para este estudiante");
        }

        Sesion sesion = sesionesFuturas.get(0);

        if (sesion.getEnlace() == null) {
            throw new IllegalStateException("La sesión no tiene enlace asignado");
        }

        return sesion.getEnlace().getEnlace(); // Retorna el String URL
    }
}
