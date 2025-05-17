package tutorgo.com.tutorgo.service;

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

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final UsuarioRepository usuarioRepository;

    private static final String ROL_TUTOR = "TUTOR";
    private static final String ROL_ESTUDIANTE = "ESTUDIANTE";

    @Autowired
    public PagoService(PagoRepository pagoRepository, UsuarioRepository usuarioRepository) {
        this.pagoRepository = pagoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<PagoHistorialItemDto> getHistorialPagos(Integer usuarioIdAutenticado) {
        Usuario usuario = usuarioRepository.findById(usuarioIdAutenticado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioIdAutenticado));

        List<PagoHistorialItemDto> historial = new ArrayList<>();

        if (usuario.getRol() == null || usuario.getRol().getNombre() == null) {
            return historial;
        }

        String nombreRolUsuario = usuario.getRol().getNombre();

        if (nombreRolUsuario.equalsIgnoreCase(ROL_TUTOR)) {
            Tutor tutorPerfil = usuario.getTutor();
            if (tutorPerfil == null) {
                throw new ResourceNotFoundException("Perfil de tutor no encontrado para el usuario con ID: " + usuario.getId());
            }
            List<Pago> pagos = pagoRepository.findByTutorIdOrderByIdDesc(tutorPerfil.getId()); // Usa PK de tutores

            historial = pagos.stream().map(pago -> new PagoHistorialItemDto(
                    pago.getId(),
                    pago.getMonto().subtract(pago.getComisionPlataforma()),
                    "Ingreso por tutoría con " + pago.getEstudiante().getUsuario().getNombre(),
                    pago.getTipoEstado(),
                    "INGRESO",
                    pago.getEstudiante().getUsuario().getNombre()
            )).collect(Collectors.toList());

        } else if (nombreRolUsuario.equalsIgnoreCase(ROL_ESTUDIANTE)) {
            Estudiante estudiantePerfil = usuario.getEstudiante();
            if (estudiantePerfil == null) {
                throw new ResourceNotFoundException("Perfil de estudiante no encontrado para el usuario con ID: " + usuario.getId());
            }
            List<Pago> pagos = pagoRepository.findByEstudianteIdOrderByIdDesc(estudiantePerfil.getId()); // Usa PK de estudiantes

            historial = pagos.stream().map(pago -> new PagoHistorialItemDto(
                    pago.getId(),
                    pago.getMonto(),
                    "Pago por tutoría a " + pago.getTutor().getUsuario().getNombre(),
                    pago.getTipoEstado(),
                    "GASTO",
                    pago.getTutor().getUsuario().getNombre()
            )).collect(Collectors.toList());
        }
        return historial;
    }
}