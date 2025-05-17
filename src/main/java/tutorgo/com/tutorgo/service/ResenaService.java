// src/main/java/tutorgo/com/tutorgo/service/ResenaService.java
package tutorgo.com.tutorgo.service;

import tutorgo.com.tutorgo.dto.request.ResenaRequestDto;
import tutorgo.com.tutorgo.dto.response.ResenaResponseDto;
import tutorgo.com.tutorgo.model.entity.Estudiante; // Asegúrate que esté importado
import tutorgo.com.tutorgo.model.entity.Resena;
import tutorgo.com.tutorgo.model.entity.Sesion;
import tutorgo.com.tutorgo.model.entity.Tutor;
import tutorgo.com.tutorgo.model.entity.Usuario;
import tutorgo.com.tutorgo.exception.AccessDeniedException;
import tutorgo.com.tutorgo.exception.BadRequestException;
import tutorgo.com.tutorgo.exception.ResourceNotFoundException;
import tutorgo.com.tutorgo.repository.EstudianteRepository; // Asegúrate que esté importado
import tutorgo.com.tutorgo.repository.ResenaRepository;
import tutorgo.com.tutorgo.repository.SesionRepository;
import tutorgo.com.tutorgo.repository.TutorRepository;
import tutorgo.com.tutorgo.repository.UsuarioRepository;

import org.slf4j.Logger; // Para logging
import org.slf4j.LoggerFactory; // Para logging
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
// import java.util.List; // Ya no es necesario si se usa la query de promedio
// import java.util.Objects; // Ya no es necesario si se usa la query de promedio

@Service
public class ResenaService {

    private static final Logger logger = LoggerFactory.getLogger(ResenaService.class);

    private final SesionRepository sesionRepository;
    private final ResenaRepository resenaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TutorRepository tutorRepository;
    private final EstudianteRepository estudianteRepository;

    private static final String ROL_ESTUDIANTE = "ESTUDIANTE";

    @Autowired
    public ResenaService(SesionRepository sesionRepository,
                         ResenaRepository resenaRepository,
                         UsuarioRepository usuarioRepository,
                         TutorRepository tutorRepository,
                         EstudianteRepository estudianteRepository) { // Añadido
        this.sesionRepository = sesionRepository;
        this.resenaRepository = resenaRepository;
        this.usuarioRepository = usuarioRepository;
        this.tutorRepository = tutorRepository;
        this.estudianteRepository = estudianteRepository; // Añadido
    }

    @Transactional // Esta anotación es crucial para toda la operación
    public ResenaResponseDto crearResena(Integer sesionId, Integer idUsuarioSolicitanteEstudiante, ResenaRequestDto requestDto) {
        logger.info("Iniciando crearResena para sesionId: {}, solicitanteId: {}", sesionId, idUsuarioSolicitanteEstudiante);

        // 1. Verificar que el solicitante es un estudiante válido
        Usuario solicitanteUsuario = usuarioRepository.findUsuarioById(idUsuarioSolicitanteEstudiante)
                .orElseThrow(() -> new AccessDeniedException("Usuario solicitante no encontrado con ID: " + idUsuarioSolicitanteEstudiante));
        logger.debug("Solicitante encontrado: {}", solicitanteUsuario.getEmail());

        if (solicitanteUsuario.getRol() == null || !solicitanteUsuario.getRol().getNombre().equalsIgnoreCase(ROL_ESTUDIANTE)) {
            logger.warn("Usuario {} no es un estudiante autorizado (rol incorrecto o nulo)", idUsuarioSolicitanteEstudiante);
            throw new AccessDeniedException("El solicitante no es un estudiante autorizado.");
        }
        Estudiante perfilEstudianteSolicitante = solicitanteUsuario.getEstudiante();
        if (perfilEstudianteSolicitante == null) {
            logger.warn("Usuario {} (rol ESTUDIANTE) no tiene perfil de estudiante asociado.", idUsuarioSolicitanteEstudiante);
            throw new AccessDeniedException("El solicitante no tiene un perfil de estudiante.");
        }
        logger.debug("Perfil de estudiante del solicitante encontrado: id={}", perfilEstudianteSolicitante.getId());


        // 2. Encontrar la sesión y VERIFICAR QUE PERTENECE AL ESTUDIANTE SOLICITANTE
        //    SesionRepository.findByIdAndEstudianteId espera el estudiantes.id (PK del perfil Estudiante)
        Sesion sesion = sesionRepository.findByIdAndEstudianteId(sesionId, perfilEstudianteSolicitante.getId())
                .orElseThrow(() -> {
                    logger.warn("Sesión no encontrada con ID: {} para el estudiante con perfil ID: {}", sesionId, perfilEstudianteSolicitante.getId());
                    return new ResourceNotFoundException("Sesión no encontrada con ID: " + sesionId + " para el estudiante solicitante.");
                });
        logger.debug("Sesión encontrada: id={}", sesion.getId());

        // 3. Verificar si la sesión está completada
        Timestamp ahora = Timestamp.from(Instant.now());
        if (sesion.getHoraFinal() == null || sesion.getHoraFinal().after(ahora)) {
            logger.warn("Intento de calificar sesión no completada: sesionId={}", sesionId);
            throw new BadRequestException("Solo se pueden calificar sesiones completadas.");
        }

        // 4. Verificar que la sesión no haya sido calificada antes
        if (resenaRepository.existsBySesionId(sesionId)) {
            logger.warn("Intento de calificar sesión ya calificada: sesionId={}", sesionId);
            throw new BadRequestException("Esta sesión ya ha sido calificada.");
        }

        // (Validaciones de DTO ya hechas por @Valid en el controlador)

        // 5. Crear y guardar la reseña
        Resena resena = new Resena();
        resena.setSesion(sesion);
        resena.setCalificacion(requestDto.getCalificacion());
        resena.setComentario(requestDto.getComentario());
        Resena savedResena = resenaRepository.save(resena);
        logger.info("Reseña guardada con ID: {} para sesionId: {}", savedResena.getId(), sesionId);

        // 6. Actualizar promedio de estrellas del tutor
        Tutor tutorDeLaSesion = sesion.getTutor(); // Esto devuelve la entidad Tutor de la sesión
        if (tutorDeLaSesion != null) {
            logger.debug("Actualizando promedio de estrellas para tutorId: {}", tutorDeLaSesion.getId());
            actualizarPromedioEstrellasTutor(tutorDeLaSesion);
        } else {
            logger.warn("No se pudo obtener el tutor de la sesión con ID: {} para actualizar promedio de estrellas.", sesionId);
        }

        return new ResenaResponseDto(
                savedResena.getId(),
                savedResena.getSesion().getId(),
                savedResena.getCalificacion(),
                savedResena.getComentario()
        );
    }

    // Método privado para actualizar el promedio de estrellas del tutor
    private void actualizarPromedioEstrellasTutor(Tutor tutor) {
        if (tutor == null || tutor.getId() == null) {
            logger.warn("Intento de actualizar promedio de estrellas para un tutor nulo o sin ID.");
            return;
        }
        // Usar el método del repositorio para calcular el promedio
        Float nuevoPromedio = resenaRepository.calcularPromedioCalificacionesPorTutorId(tutor.getId());
        logger.info("Nuevo promedio calculado para tutorId {}: {}", tutor.getId(), nuevoPromedio);

        // Asegurarse de que el promedio no sea nulo (COALESCE en la query ya debería manejar esto)
        float promedioAAsignar = (nuevoPromedio != null) ? nuevoPromedio : 0.0f;

        // Redondear a un decimal si es necesario (opcional, depende de cómo quieras mostrarlo)
        promedioAAsignar = Math.round(promedioAAsignar * 10.0f) / 10.0f;

        tutor.setEstrellasPromedio(promedioAAsignar);
        tutorRepository.save(tutor); // Guardar el tutor actualizado
        logger.info("Promedio de estrellas actualizado para tutorId {} a: {}", tutor.getId(), tutor.getEstrellasPromedio());
    }
}