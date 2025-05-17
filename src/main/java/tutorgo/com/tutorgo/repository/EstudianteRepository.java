package tutorgo.com.tutorgo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tutorgo.com.tutorgo.model.entity.Estudiante;

import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {
    
    Optional<Estudiante> findByUsuarioId(Integer usuarioId);
    
    @Query("SELECT e FROM Estudiante e JOIN e.usuario u WHERE u.email = :email")
    Optional<Estudiante> findByUsuarioEmail(String email);
}
