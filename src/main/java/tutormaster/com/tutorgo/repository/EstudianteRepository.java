package tutormaster.com.tutorgo.repository;
import tutormaster.com.tutorgo.model.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {
    Optional<Estudiante> findByUsuarioId(Integer usuarioId);
}