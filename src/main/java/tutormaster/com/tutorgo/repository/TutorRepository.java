package tutormaster.com.tutorgo.repository;
import tutormaster.com.tutorgo.model.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface TutorRepository extends JpaRepository<Tutor, Integer> {
    Optional<Tutor> findByUsuarioId(Integer usuarioId);
}