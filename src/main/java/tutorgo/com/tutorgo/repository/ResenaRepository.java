package tutorgo.com.tutorgo.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tutorgo.com.tutorgo.model.entity.Resena;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResenaRepository extends JpaRepository<Resena, Integer> {
    boolean existsBySesionId(Integer sesionId);
    @Query("SELECT COALESCE(AVG(r.calificacion), 0.0) FROM Resena r WHERE r.sesion.tutor.id = :tutorId")
    Float calcularPromedioCalificacionesPorTutorId(@Param("tutorId") Integer tutorId);
}
