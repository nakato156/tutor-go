package tutormaster.com.tutorgo.repository;

import tutormaster.com.tutorgo.model.entity.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface SesionRepository extends JpaRepository<Sesion, Integer> {
    // Sesion.tutorUsuario.id es el Usuarios.id del tutor
    @Query("SELECT s FROM Sesion s WHERE s.id = :sesionId AND s.tutor.id = :usuarioTutorId")
    Optional<Sesion> findByIdAndTutorUsuarioId(Integer sesionId, Integer usuarioTutorId);

    // Sesion.estudianteUsuario.id es el Usuarios.id del estudiante
    @Query("SELECT s FROM Sesion s WHERE s.id = :sesionId AND s.estudiante.id = :usuarioEstudianteId")
    Optional<Sesion> findByIdAndEstudianteUsuarioId(Integer sesionId, Integer usuarioEstudianteId);
}