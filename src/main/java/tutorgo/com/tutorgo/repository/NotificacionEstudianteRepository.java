package tutorgo.com.tutorgo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tutorgo.com.tutorgo.model.entity.Notificacion_Estudiante;

import java.util.List;

@Repository
public interface NotificacionEstudianteRepository extends JpaRepository<Notificacion_Estudiante, Integer> {

    List<Notificacion_Estudiante> findByEstudianteId(Integer estudianteId);

}
