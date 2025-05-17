package tutorgo.com.tutorgo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tutorgo.com.tutorgo.model.entity.Rol;

public interface RolRepository extends JpaRepository<Rol,Integer> {
    Optional<Rol> findByNombre(String nombre);
}
