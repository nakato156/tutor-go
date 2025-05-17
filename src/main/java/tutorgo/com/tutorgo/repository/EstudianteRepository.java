package tutorgo.com.tutorgo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tutorgo.com.tutorgo.model.entity.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {
}
