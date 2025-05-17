package tutormaster.com.tutorgo.service;

import tutormaster.com.tutorgo.dto.request.AdjuntarEnlacesRequestDto;
import tutormaster.com.tutorgo.dto.EnlaceDto;
import tutormaster.com.tutorgo.model.entity.EnlaceSesion;
import tutormaster.com.tutorgo.model.entity.Sesion;
import tutormaster.com.tutorgo.model.entity.Usuario;
import tutormaster.com.tutorgo.model.enums.EstadoSesion;
import tutormaster.com.tutorgo.exception.AccessDeniedException;
import tutormaster.com.tutorgo.exception.BadRequestException;
import tutormaster.com.tutorgo.exception.ResourceNotFoundException;
import tutormaster.com.tutorgo.repository.EnlaceSesionRepository;
import tutormaster.com.tutorgo.repository.SesionRepository;
import tutormaster.com.tutorgo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired; // Importar si se usa @Autowired


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnlaceSesionService {

    private final SesionRepository sesionRepository;
    private final EnlaceSesionRepository enlaceSesionRepository;
    private final UsuarioRepository usuarioRepository;

    // Constructor explícito para inyección de dependencias
    @Autowired // Opcional si es el único constructor en versiones recientes de Spring
    public EnlaceSesionService(SesionRepository sesionRepository,
                               EnlaceSesionRepository enlaceSesionRepository,
                               UsuarioRepository usuarioRepository) {
        this.sesionRepository = sesionRepository;
        this.enlaceSesionRepository = enlaceSesionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public List<EnlaceDto> adjuntarEnlaces(Integer sesionId, Integer usuarioTutorId, AdjuntarEnlacesRequestDto requestDto) {
        Usuario tutorUser = usuarioRepository.findById(usuarioTutorId)
                .orElseThrow(() -> new AccessDeniedException("Usuario tutor no encontrado."));
        if (tutorUser.getTutor() == null) {
            throw new AccessDeniedException("El usuario no es un tutor autorizado.");
        }

        Sesion sesion = sesionRepository.findByIdAndTutorUsuarioId(sesionId, usuarioTutorId)
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
                .map(enlace -> {
                    EnlaceDto dto = new EnlaceDto();
                    dto.setNombre(enlace.getNombre());
                    dto.setEnlace(enlace.getEnlace());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}