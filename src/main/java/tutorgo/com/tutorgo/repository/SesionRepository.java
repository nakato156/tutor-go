package tutorgo.com.tutorgo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tutorgo.com.tutorgo.model.entity.Sesion;
import java.util.Optional;

public interface SesionRepository extends JpaRepository<Sesion, Integer> {
    Optional<Sesion> findByIdAndEstudianteId(Integer sesionId, Integer estudianteId);
}
