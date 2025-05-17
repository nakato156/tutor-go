package tutormaster.com.tutorgo.service;

import tutormaster.com.tutorgo.dto.PagoHistorialItemDto;
import tutormaster.com.tutorgo.model.entity.Pago;
import tutormaster.com.tutorgo.model.entity.Usuario;
import tutormaster.com.tutorgo.model.entity.Tutor;
import tutormaster.com.tutorgo.model.entity.Estudiante;
import tutormaster.com.tutorgo.model.enums.EstadoPago;
import tutormaster.com.tutorgo.exception.ResourceNotFoundException;
import tutormaster.com.tutorgo.repository.PagoRepository;
import tutormaster.com.tutorgo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


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
                throw new ResourceNotFoundException("Perfil de tutor no encontrado para el usuario.");
            }
            // Usar el método del repositorio que ordena por ID
            List<Pago> pagos = pagoRepository.findByTutorIdOrderByIdDesc(tutorPerfil.getId());

//            historial = pagos.stream().map(pago -> new PagoHistorialItemDto(
//                    pago.getId(),
//                    // pago.getFechaTransaccion(), // ELIMINADO
//                    pago.getMonto().subtract(pago.getComisionPlataforma()),
//                    "Ingreso por tutoría con " + pago.getEstudiante().getUsuario().getNombre(),
//                    pago.getTipoEstado(),
//                    "INGRESO",
//                    pago.getEstudiante().getUsuario().getNombre()
//            )).collect(Collectors.toList());

        } else if (nombreRolUsuario.equalsIgnoreCase(ROL_ESTUDIANTE)) {
            Estudiante estudiantePerfil = usuario.getEstudiante();
            if (estudiantePerfil == null) {
                throw new ResourceNotFoundException("Perfil de estudiante no encontrado para el usuario.");
            }
            // Usar el método del repositorio que ordena por ID
            List<Pago> pagos = pagoRepository.findByEstudianteIdOrderByIdDesc(estudiantePerfil.getId());

//            historial = pagos.stream().map(pago -> new PagoHistorialItemDto(
//                    pago.getId(),
//                    // pago.getFechaTransaccion(), // ELIMINADO
//                    pago.getMonto(),
//                    "Pago por tutoría a " + pago.getTutor().getUsuario().getNombre(),
//                    pago.getTipoEstado(),
//                    "GASTO",
//                    pago.getTutor().getUsuario().getNombre()
//            )).collect(Collectors.toList());
        }
        return historial;
    }
}