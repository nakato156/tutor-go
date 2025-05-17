package tutormaster.com.tutorgo.service;

import tutormaster.com.tutorgo.dto.request.ResenaRequestDto;
import tutormaster.com.tutorgo.dto.response.ResenaResponseDto; // Asegúrate que el paquete del DTO sea correcto
import tutormaster.com.tutorgo.model.entity.Resena;
import tutormaster.com.tutorgo.model.entity.Sesion;
import tutormaster.com.tutorgo.model.entity.Tutor;
import tutormaster.com.tutorgo.model.entity.Usuario;
import tutormaster.com.tutorgo.exception.AccessDeniedException;
import tutormaster.com.tutorgo.exception.BadRequestException;
import tutormaster.com.tutorgo.exception.ResourceNotFoundException;
import tutormaster.com.tutorgo.repository.ResenaRepository;
import tutormaster.com.tutorgo.repository.SesionRepository;
import tutormaster.com.tutorgo.repository.TutorRepository;
import tutormaster.com.tutorgo.repository.UsuarioRepository;

// import lombok.RequiredArgsConstructor; // Comentado o eliminado
import org.springframework.beans.factory.annotation.Autowired; // Importar si se usa @Autowired en el constructor
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
// @RequiredArgsConstructor // Comentado o eliminado
public class ResenaService {

    private final SesionRepository sesionRepository;
    private final ResenaRepository resenaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TutorRepository tutorRepository;

    private static final String ROL_ESTUDIANTE = "ESTUDIANTE";

    // Constructor explícito para inyección de dependencias
    @Autowired // Opcional si es el único constructor en versiones recientes de Spring
    public ResenaService(SesionRepository sesionRepository,
                         ResenaRepository resenaRepository,
                         UsuarioRepository usuarioRepository,
                         TutorRepository tutorRepository) {
        this.sesionRepository = sesionRepository;
        this.resenaRepository = resenaRepository;
        this.usuarioRepository = usuarioRepository;
        this.tutorRepository = tutorRepository;
    }

    @Transactional
    public ResenaResponseDto crearResena(Integer sesionId, Integer usuarioEstudianteId, ResenaRequestDto requestDto) {
        Usuario estudianteUser = usuarioRepository.findById(usuarioEstudianteId)
                .orElseThrow(() -> new AccessDeniedException("Usuario estudiante no encontrado."));

        if (estudianteUser.getRol() == null || !estudianteUser.getRol().getNombre().equalsIgnoreCase(ROL_ESTUDIANTE) || estudianteUser.getEstudiante() == null) {
            throw new AccessDeniedException("El usuario no es un estudiante autorizado o no tiene perfil de estudiante.");
        }

        Sesion sesion = sesionRepository.findByIdAndEstudianteUsuarioId(sesionId, usuarioEstudianteId)
                .orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada con ID: " + sesionId + " para el estudiante especificado."));

        Timestamp ahora = Timestamp.from(Instant.now());
        if (sesion.getHoraFinal() == null || sesion.getHoraFinal().after(ahora)) {
            throw new BadRequestException("Solo se pueden calificar sesiones completadas.");
        }

        if (resenaRepository.existsBySesionId(sesionId)) {
            throw new BadRequestException("Esta sesión ya ha sido calificada.");
        }

        if (requestDto.getCalificacion() < 1 || requestDto.getCalificacion() > 5) {
            throw new BadRequestException("La calificación debe ser entre 1 y 5.");
        }
        if (requestDto.getComentario() != null && requestDto.getComentario().length() > 500) {
            throw new BadRequestException("El comentario no puede exceder los 500 caracteres.");
        }

        Resena resena = new Resena();
        resena.setSesion(sesion);
        resena.setCalificacion(requestDto.getCalificacion());
        resena.setComentario(requestDto.getComentario());
        Resena savedResena = resenaRepository.save(resena);

        Tutor tutorDeLaSesionUsuario = sesion.getTutor();
        if (tutorDeLaSesionUsuario != null && tutorDeLaSesionUsuario != null) {
            actualizarPromedioEstrellasTutor(tutorDeLaSesionUsuario);
        }

        return new ResenaResponseDto( // Asumiendo que ResenaResponseDto tiene este constructor
                savedResena.getId(),
                savedResena.getSesion().getId(),
                savedResena.getCalificacion(),
                savedResena.getComentario()
        );
    }

    private void actualizarPromedioEstrellasTutor(Tutor tutor) {
        Usuario tutorUsuario = tutor.getUsuario();
        if (tutorUsuario == null || tutorUsuario.getSesionesComoTutor() == null) {
            tutor.setEstrellasPromedio(0.0f);
            tutorRepository.save(tutor);
            return;
        }

        List<Resena> resenasDelTutor = tutorUsuario.getSesionesComoTutor().stream()
                .map(Sesion::getResena)
                .filter(Objects::nonNull)
                .toList();

        if (!resenasDelTutor.isEmpty()) {
            double promedio = resenasDelTutor.stream()
                    .mapToInt(Resena::getCalificacion)
                    .average()
                    .orElse(0.0);
            tutor.setEstrellasPromedio((float) Math.round(promedio * 10.0) / 10.0f);
        } else {
            tutor.setEstrellasPromedio(0.0f);
        }
        tutorRepository.save(tutor);
    }
}
