package tutorgo.com.tutorgo.service;

import tutorgo.com.tutorgo.model.entity.Notificacion_Estudiante;
import tutorgo.com.tutorgo.model.entity.Sesion;

import java.util.List;

public interface AccountService {
    String obtenerEnlaceSesionActiva(Integer estudianteId);
    List<Sesion> obtenerSesionesDesde2023(Integer estudianteId);

    // Nuevo método para obtener notificaciones
    List<Notificacion_Estudiante> obtenerNotificaciones(Integer estudianteId);
}
