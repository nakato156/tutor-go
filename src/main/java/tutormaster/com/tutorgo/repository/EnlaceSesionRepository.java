package tutormaster.com.tutorgo.repository;
import tutormaster.com.tutorgo.model.entity.EnlaceSesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface EnlaceSesionRepository extends JpaRepository<EnlaceSesion, Integer> {
    List<EnlaceSesion> findBySesionId(Integer sesionId);
    @Transactional
    void deleteBySesionId(Integer sesionId);
}