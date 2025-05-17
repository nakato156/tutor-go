package tutorgo.com.tutorgo.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tutorgo.com.tutorgo.model.entity.Notificacion_Estudiante;
import tutorgo.com.tutorgo.model.entity.Sesion;
import tutorgo.com.tutorgo.model.enums.TipoNotificacion;
import tutorgo.com.tutorgo.repository.NotificacionEstudianteRepository;
import tutorgo.com.tutorgo.repository.SesionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecordatorioSesionScheduler {

    private final SesionRepository sesionRepository;
    private final NotificacionEstudianteRepository notificacionRepo;

    // Ejecuta cada hora
    @Scheduled(cron = "0 0 * * * *")
    public void enviarRecordatorios() {
        LocalDateTime desde = LocalDateTime.now().plusHours(24).minusMinutes(30);
        LocalDateTime hasta = LocalDateTime.now().plusHours(24).plusMinutes(30);

        List<Sesion> sesiones = sesionRepository.findSesionesDentroDeRango(desde, hasta);

        for (Sesion sesion : sesiones) {
            Notificacion_Estudiante notificacion = new Notificacion_Estudiante();
            notificacion.setTitulo("Recordatorio de sesión");
            notificacion.setTexto("Tienes una sesión programada el " +
                    sesion.getFecha().toLocalDate() + " a las " +
                    sesion.getHoraInicial().toLocalDateTime().toLocalTime());
            notificacion.setTipo(TipoNotificacion.RECORDATORIO);
            notificacion.setEstudiante(sesion.getEstudiante());

            notificacionRepo.save(notificacion);
        }

        log.info("Notificaciones de recordatorio enviadas para {} sesión(es)", sesiones.size());
    }
}
