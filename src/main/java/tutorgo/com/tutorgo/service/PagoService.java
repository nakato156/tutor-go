package tutorgo.com.tutorgo.service;

import org.springframework.transaction.annotation.Transactional;
import tutorgo.com.tutorgo.dto.PagoHistorialItemDto;
import tutorgo.com.tutorgo.model.entity.Estudiante;
import tutorgo.com.tutorgo.model.entity.Pago;
import tutorgo.com.tutorgo.model.entity.Tutor;
import tutorgo.com.tutorgo.model.entity.Usuario;
import tutorgo.com.tutorgo.exception.ResourceNotFoundException;
import tutorgo.com.tutorgo.repository.PagoRepository;
import tutorgo.com.tutorgo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PagoService {

    private static final Logger logger = LoggerFactory.getLogger(PagoService.class); // Logger

    private final PagoRepository pagoRepository;
    private final UsuarioRepository usuarioRepository;

    private static final String ROL_TUTOR = "TUTOR";
    private static final String ROL_ESTUDIANTE = "ESTUDIANTE";

    @Autowired
    public PagoService(PagoRepository pagoRepository, UsuarioRepository usuarioRepository) {
        this.pagoRepository = pagoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true) // ¡Asegúrate de que esto esté aquí!
    public List<PagoHistorialItemDto> getHistorialPagos(Integer usuarioIdAutenticado) {
        logger.info("INICIO getHistorialPagos para usuarioIdAutenticado: {}", usuarioIdAutenticado);

        Usuario usuario = usuarioRepository.findUsuarioById(usuarioIdAutenticado)
                .orElseThrow(() -> {
                    logger.error("Usuario no encontrado con ID: {}", usuarioIdAutenticado);
                    return new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioIdAutenticado);
                });
        logger.info("Usuario recuperado: id={}, email={}, rolId={}",
                usuario.getId(), usuario.getEmail(), (usuario.getRol() != null ? usuario.getRol().getId() : "NULL_ROL_ID"));

        if (usuario.getRol() == null || usuario.getRol().getNombre() == null) {
            logger.warn("Usuario id={} no tiene rol o nombre de rol es nulo. Devolviendo historial vacío.", usuario.getId());
            return new ArrayList<>();
        }
        logger.info("Nombre del rol del usuario: {}", usuario.getRol().getNombre());

        List<PagoHistorialItemDto> historial = new ArrayList<>();
        String nombreRolUsuario = usuario.getRol().getNombre();

        if (nombreRolUsuario.equalsIgnoreCase(ROL_TUTOR)) {
            logger.info("Usuario es TUTOR. Intentando obtener perfil de tutor...");
            Tutor tutorPerfil = usuario.getTutor();
            if (tutorPerfil == null) {
                logger.error("PERFIL DE TUTOR ES NULL para usuarioId: {}. Aunque el rol es TUTOR.", usuario.getId());
                // throw new ResourceNotFoundException("Perfil de tutor no encontrado para el usuario con ID: " + usuario.getId());
                return historial; // Si el perfil es null, no podemos buscar pagos
            }
            logger.info("Perfil de Tutor encontrado: tutor.id={}", tutorPerfil.getId());

            List<Pago> pagos = pagoRepository.findByTutorIdOrderByIdDesc(tutorPerfil.getId());
            logger.info("Número de pagos encontrados para tutor.id {}: {}", tutorPerfil.getId(), pagos.size());

            if (pagos.isEmpty()) {
                logger.info("No se encontraron pagos en la BD para el tutor.id {}", tutorPerfil.getId());
            }

            historial = pagos.stream().map(pago -> {
                logger.debug("Mapeando pago.id: {} para DTO de tutor", pago.getId());
                String nombreEstudiante = (pago.getEstudiante() != null && pago.getEstudiante().getUsuario() != null) ?
                        pago.getEstudiante().getUsuario().getNombre() : "Estudiante Desconocido";
                return new PagoHistorialItemDto(
                        pago.getId(),
                        pago.getMonto().subtract(pago.getComisionPlataforma()),
                        "Ingreso por tutoría con " + nombreEstudiante,
                        pago.getTipoEstado(),
                        "INGRESO",
                        nombreEstudiante
                );
            }).collect(Collectors.toList());

        } else if (nombreRolUsuario.equalsIgnoreCase(ROL_ESTUDIANTE)) {
            logger.info("Usuario es ESTUDIANTE. Intentando obtener perfil de estudiante...");
            Estudiante estudiantePerfil = usuario.getEstudiante();
            if (estudiantePerfil == null) {
                logger.error("PERFIL DE ESTUDIANTE ES NULL para usuarioId: {}. Aunque el rol es ESTUDIANTE.", usuario.getId());
                // throw new ResourceNotFoundException("Perfil de estudiante no encontrado para el usuario con ID: " + usuario.getId());
                return historial;
            }
            logger.info("Perfil de Estudiante encontrado: estudiante.id={}", estudiantePerfil.getId());

            List<Pago> pagos = pagoRepository.findByEstudianteIdOrderByIdDesc(estudiantePerfil.getId());
            logger.info("Número de pagos encontrados para estudiante.id {}: {}", estudiantePerfil.getId(), pagos.size());

            if (pagos.isEmpty()) {
                logger.info("No se encontraron pagos en la BD para el estudiante.id {}", estudiantePerfil.getId());
            }

            historial = pagos.stream().map(pago -> {
                logger.debug("Mapeando pago.id: {} para DTO de estudiante", pago.getId());
                String nombreTutor = (pago.getTutor() != null && pago.getTutor().getUsuario() != null) ?
                        pago.getTutor().getUsuario().getNombre() : "Tutor Desconocido";
                return new PagoHistorialItemDto(
                        pago.getId(),
                        pago.getMonto(),
                        "Pago por tutoría a " + nombreTutor,
                        pago.getTipoEstado(),
                        "GASTO",
                        nombreTutor
                );
            }).collect(Collectors.toList());
        }
        logger.info("FINAL getHistorialPagos para usuarioIdAutenticado: {}. Tamaño del historial: {}", usuarioIdAutenticado, historial.size());
        return historial;
    }
}