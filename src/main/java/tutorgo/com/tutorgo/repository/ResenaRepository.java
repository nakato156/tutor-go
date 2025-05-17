package tutorgo.com.tutorgo.repository;
import tutorgo.com.tutorgo.model.entity.Resena;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResenaRepository extends JpaRepository<Resena, Integer> {
    boolean existsBySesionId(Integer sesionId);
}
