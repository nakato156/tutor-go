package tutorgo.com.tutorgo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import tutorgo.com.tutorgo.dto.request.ReservaTutoriaRequest;
import tutorgo.com.tutorgo.dto.response.DisponibilidadResponse;
import tutorgo.com.tutorgo.dto.response.SesionResponse;
import tutorgo.com.tutorgo.model.entity.Disponibilidad;
import tutorgo.com.tutorgo.model.entity.Estudiante;
import tutorgo.com.tutorgo.model.entity.Sesion;
import tutorgo.com.tutorgo.model.entity.Tutor;
import tutorgo.com.tutorgo.model.enums.TipoEstadoSesiones;
import tutorgo.com.tutorgo.repository.DisponibilidadRepository;
import tutorgo.com.tutorgo.repository.EstudianteRepository;
import tutorgo.com.tutorgo.repository.SesionRepository;
import tutorgo.com.tutorgo.repository.TutorRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TutoriaService {

    private final DisponibilidadRepository disponibilidadRepository;
    private final SesionRepository sesionRepository;
    private final EstudianteRepository estudianteRepository;
    private final TutorRepository tutorRepository;

    @Autowired
    public TutoriaService(
            DisponibilidadRepository disponibilidadRepository,
            SesionRepository sesionRepository,
            EstudianteRepository estudianteRepository,
            TutorRepository tutorRepository) {
        this.disponibilidadRepository = disponibilidadRepository;
        this.sesionRepository = sesionRepository;
        this.estudianteRepository = estudianteRepository;
        this.tutorRepository = tutorRepository;
    }

    public List<DisponibilidadResponse> getDisponibilidadesByTutor(Integer tutorId) {
        LocalDateTime ahora = LocalDateTime.now();
        List<Disponibilidad> disponibilidades = disponibilidadRepository.findAvailableByTutorIdAndFutureDate(tutorId, ahora);
        List<Sesion> sesiones = sesionRepository.findByTutorIdOrderByFechaDescHoraInicialDesc(tutorId);

        return disponibilidades.stream()
                .filter(d -> isDisponible(d, sesiones))
                .map(this::mapToDisponibilidadResponse)
                .collect(Collectors.toList());
    }

    private boolean isDisponible(Disponibilidad disponibilidad, List<Sesion> sesiones) {
        LocalDate fecha = disponibilidad.getFecha();
        return sesiones.stream().noneMatch(sesion ->
                sesion.getFecha().toLocalDate().equals(fecha) &&
                        horariosSeSolapan(sesion.getHoraInicial(), sesion.getHoraFinal(),
                                disponibilidad.getHoraInicial(), disponibilidad.getHoraFinal())
        );
    }

    private boolean horariosSeSolapan(LocalDateTime inicio1, LocalDateTime fin1,
                                      LocalDateTime inicio2, LocalDateTime fin2) {
        return inicio1.isBefore(fin2) && fin1.isAfter(inicio2);
    }

    @Transactional
    public SesionResponse reservarTutoria(ReservaTutoriaRequest request) {
        try {
            // 1. Verificar que la disponibilidad exista
            Disponibilidad disponibilidad = disponibilidadRepository.findById(request.getDisponibilidadId())
                    .orElseThrow(() -> new IllegalArgumentException("La disponibilidad seleccionada no existe"));
        
            // 2. Verificar que el estudiante exista
            Estudiante estudiante = estudianteRepository.findById(request.getEstudianteId())
                    .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));
        
            // 3. Verificar si ya existe una sesión para este estudiante y disponibilidad (verificación más estricta)
            boolean sesionExistente = sesionRepository.existsByEstudianteIdAndTutorIdAndHoraInicialAndHoraFinal(
                    estudiante.getId(),
                    disponibilidad.getTutor().getId(),
                    disponibilidad.getHoraInicial(),
                    disponibilidad.getHoraFinal()
            );
            
            if (sesionExistente) {
                throw new IllegalArgumentException("Ya has reservado una tutoría en este horario");
            }
        
            // 4. Verificar si existe alguna sesión que se solape con este horario usando una consulta directa
            long sesionesSuperpuestas = sesionRepository.countSesionesSuperpuestas(
                    disponibilidad.getTutor().getId(),
                    disponibilidad.getHoraInicial(),
                    disponibilidad.getHoraFinal()
            );
            
            if (sesionesSuperpuestas > 0) {
                throw new IllegalArgumentException("El tutor ya tiene una sesión programada en este horario. Por favor, elige otra disponibilidad.");
            }
        
            // 5. Si todo está bien, crear la sesión usando el constructor
            Sesion nuevaSesion = Sesion.builder()
                    .fecha(disponibilidad.getHoraInicial())
                    .horaInicial(disponibilidad.getHoraInicial())
                    .horaFinal(disponibilidad.getHoraFinal())
                    .tipoEstado(TipoEstadoSesiones.PENDIENTE)
                    .tutor(disponibilidad.getTutor())
                    .estudiante(estudiante)
                    .build();
            
            // Guardar la sesión
            Sesion sesionGuardada = sesionRepository.save(nuevaSesion);
            
            // Registrar ID generado para depuración
            System.out.println("Sesión creada exitosamente con ID: " + sesionGuardada.getId());
            
            return mapToSesionResponse(sesionGuardada);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Error de integridad de datos: " + e.getMessage());
            e.printStackTrace();
            
            // Mensaje más específico para problemas con la secuencia o ID
            if (e.getMessage() != null && e.getMessage().contains("sesiones_pkey")) {
                throw new IllegalArgumentException("Error al generar ID único. Por favor, intenta de nuevo en unos momentos.");
            }
            
            // Verificar si es un error de llave duplicada y dar un mensaje más específico
            if (e.getMessage() != null && e.getMessage().contains("llave duplicada")) {
                throw new IllegalArgumentException("Ya existe una reserva para este horario. Por favor, elige otra disponibilidad.");
            }
            
            throw new IllegalArgumentException("No se pudo completar la reserva debido a un conflicto de datos. Por favor, intenta nuevamente.");
        } catch (IllegalArgumentException e) {
            // Reenviar excepciones de validación
            throw e;
        } catch (Exception e) {
            System.out.println("Error general al guardar la sesión: " + e.getMessage());
            e.printStackTrace();
            throw new IllegalArgumentException("Error interno al procesar la reserva. Por favor, intenta nuevamente más tarde.");
        }
    }

    public List<SesionResponse> getSesionesByEstudiante(Integer estudianteId) {
        return sesionRepository.findByEstudianteIdOrderByFechaDescHoraInicialDesc(estudianteId)
                .stream()
                .map(this::mapToSesionResponse)
                .collect(Collectors.toList());
    }

    public List<SesionResponse> getSesionesPendientesByEstudiante(Integer estudianteId) {
        return sesionRepository.findByEstudianteIdAndTipoEstadoOrderByFechaDescHoraInicialDesc(estudianteId, TipoEstadoSesiones.PENDIENTE)
                .stream()
                .map(this::mapToSesionResponse)
                .collect(Collectors.toList());
    }

    private DisponibilidadResponse mapToDisponibilidadResponse(Disponibilidad d) {
        return DisponibilidadResponse.builder()
                .id(d.getId())
                .fecha(d.getFecha())
                .horaInicial(d.getHoraInicial())
                .horaFinal(d.getHoraFinal())
                .tutorId(d.getTutor().getId())
                .tutorNombre(d.getTutor().getUsuario().getNombre())
                .disponible(true)
                .build();
    }

    private SesionResponse mapToSesionResponse(Sesion s) {
        return SesionResponse.builder()
                .id(s.getId())
                .fecha(s.getFecha())
                .horaInicial(s.getHoraInicial())
                .horaFinal(s.getHoraFinal())
                .tipoEstado(s.getTipoEstado().name())
                .tutorId(s.getTutor().getId())
                .tutorNombre(s.getTutor().getUsuario().getNombre())
                .estudianteId(s.getEstudiante().getId())
                .build();
    }
}
