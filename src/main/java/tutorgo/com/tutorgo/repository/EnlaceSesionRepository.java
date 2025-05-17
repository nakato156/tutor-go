package tutorgo.com.tutorgo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tutorgo.com.tutorgo.model.entity.EnlaceSesion;

import java.util.Optional;

public interface EnlaceSesionRepository extends JpaRepository<EnlaceSesion, Integer> {
    Optional<EnlaceSesion> findBySesionId(Integer sesionId);
}
