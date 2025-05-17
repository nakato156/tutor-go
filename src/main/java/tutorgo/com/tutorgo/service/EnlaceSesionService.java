package tutorgo.com.tutorgo.service;

import tutorgo.com.tutorgo.dto.request.AdjuntarEnlacesRequestDto;
import tutorgo.com.tutorgo.dto.EnlaceDto;
import tutorgo.com.tutorgo.model.entity.EnlaceSesion;
import tutorgo.com.tutorgo.model.entity.Sesion;
import tutorgo.com.tutorgo.model.entity.Tutor;
import tutorgo.com.tutorgo.model.entity.Usuario;
import tutorgo.com.tutorgo.model.enums.EstadoSesion;
import tutorgo.com.tutorgo.exception.AccessDeniedException;
import tutorgo.com.tutorgo.exception.BadRequestException;
import tutorgo.com.tutorgo.exception.ResourceNotFoundException;
import tutorgo.com.tutorgo.repository.EnlaceSesionRepository;
import tutorgo.com.tutorgo.repository.SesionRepository;
import tutorgo.com.tutorgo.repository.TutorRepository;
import tutorgo.com.tutorgo.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnlaceSesionService {

    private final SesionRepository sesionRepository;
    private final EnlaceSesionRepository enlaceSesionRepository;
    private final UsuarioRepository usuarioRepository;
    private final TutorRepository tutorRepository; // Añadido

    private static final String ROL_TUTOR = "TUTOR";

    @Autowired
    public EnlaceSesionService(SesionRepository sesionRepository,
                               EnlaceSesionRepository enlaceSesionRepository,
                               UsuarioRepository usuarioRepository,
                               TutorRepository tutorRepository) { // Añadido
        this.sesionRepository = sesionRepository;
        this.enlaceSesionRepository = enlaceSesionRepository;
        this.usuarioRepository = usuarioRepository;
        this.tutorRepository = tutorRepository; // Añadido
    }

    @Transactional
    public List<EnlaceDto> adjuntarEnlaces(Integer sesionId, Integer usuarioTutorId, AdjuntarEnlacesRequestDto requestDto) {
        Usuario user = usuarioRepository.findById(usuarioTutorId)
                .orElseThrow(() -> new AccessDeniedException("Usuario no encontrado."));
        if (user.getRol() == null || !user.getRol().getNombre().equalsIgnoreCase(ROL_TUTOR)) {
            throw new AccessDeniedException("El usuario no es un tutor autorizado.");
        }
        Tutor tutor = tutorRepository.findByUsuarioId(usuarioTutorId)
                .orElseThrow(() -> new AccessDeniedException("Perfil de tutor no encontrado para el usuario."));

        Sesion sesion = sesionRepository.findByIdAndTutorId(sesionId, tutor.getId()) // Cambio aquí
                .orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada con ID: " + sesionId + " para el tutor especificado."));

        if (sesion.getTipoEstado() != EstadoSesion.CONFIRMADO) {
            throw new BadRequestException("Solo se pueden adjuntar enlaces a sesiones confirmadas (pagadas).");
        }

        for (EnlaceDto enlaceDto : requestDto.getEnlaces()) {
            if (enlaceDto.getEnlace() == null || enlaceDto.getEnlace().trim().isEmpty()) {
                throw new BadRequestException("URL inválida: el enlace no puede estar vacío.");
            }
        }

        enlaceSesionRepository.deleteBySesionId(sesionId);

        List<EnlaceSesion> nuevosEnlaces = new ArrayList<>();
        for (EnlaceDto enlaceDto : requestDto.getEnlaces()) {
            EnlaceSesion enlaceSesion = new EnlaceSesion();
            enlaceSesion.setSesion(sesion);
            enlaceSesion.setNombre(enlaceDto.getNombre());
            enlaceSesion.setEnlace(enlaceDto.getEnlace());
            nuevosEnlaces.add(enlaceSesionRepository.save(enlaceSesion));
        }

        return nuevosEnlaces.stream()
                .map(enlace -> new EnlaceDto(enlace.getNombre(), enlace.getEnlace()))
                .collect(Collectors.toList());
    }
}