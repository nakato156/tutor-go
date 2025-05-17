package tutorgo.com.tutorgo.repository;

import tutorgo.com.tutorgo.model.entity.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface SesionRepository extends JpaRepository<Sesion, Integer> {

    @Query("SELECT s FROM Sesion s WHERE s.id = :sesionId AND s.tutor.id = :tutorId")
    Optional<Sesion> findByIdAndTutorId(Integer sesionId, Integer tutorId);

    @Query("SELECT s FROM Sesion s WHERE s.id = :sesionId AND s.estudiante.id = :estudianteId")
    Optional<Sesion> findByIdAndEstudianteId(Integer sesionId, Integer estudianteId);
}