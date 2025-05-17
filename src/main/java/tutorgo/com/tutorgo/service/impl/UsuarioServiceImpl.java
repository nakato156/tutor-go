package tutorgo.com.tutorgo.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tutorgo.com.tutorgo.model.entity.Sesion;
import tutorgo.com.tutorgo.model.entity.Usuario;
import tutorgo.com.tutorgo.repository.EstudianteRepository;
import tutorgo.com.tutorgo.repository.EnlaceSesionRepository;
import tutorgo.com.tutorgo.repository.ResenaRepository;      // Import agregado
import tutorgo.com.tutorgo.repository.SesionRepository;
import tutorgo.com.tutorgo.repository.TutorRepository;
import tutorgo.com.tutorgo.repository.UsuarioRepository;
import tutorgo.com.tutorgo.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private EnlaceSesionRepository enlaceSesionRepository;

    @Transactional
    @Override
    public void eliminarPerfilUsuario(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        estudianteRepository.findByUsuarioId(usuarioId).ifPresent(estudiante -> {
            if (estudiante.getSesionesRecibidas() != null) {
                for (Sesion sesion : estudiante.getSesionesRecibidas()) {
                    // Primero borra los enlaces que refieren a cada sesión
                    enlaceSesionRepository.deleteBySesionId(sesion.getId());

                    // Ahora elimina las reseñas que referencian a esta sesión
                    resenaRepository.deleteBySesionId(sesion.getId());
                }
            }
            // Luego elimina las sesiones del estudiante
            sesionRepository.deleteByEstudianteId(estudiante.getId());

            // Finalmente elimina el estudiante
            estudianteRepository.delete(estudiante);
        });

        // Si es tutor, eliminar tutor
        tutorRepository.findByUsuarioId(usuarioId).ifPresent(tutorRepository::delete);

        // Finalmente elimina el usuario
        usuarioRepository.delete(usuario);
    }
}
