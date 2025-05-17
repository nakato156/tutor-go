package tutorgo.com.tutorgo.repository;

import tutorgo.com.tutorgo.model.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
    @Query("SELECT p FROM Pago p WHERE p.tutor.id = :tutorId ORDER BY p.id DESC")
    List<Pago> findByTutorIdOrderByIdDesc(Integer tutorId);

    @Query("SELECT p FROM Pago p WHERE p.estudiante.id = :estudianteId ORDER BY p.id DESC")
    List<Pago> findByEstudianteIdOrderByIdDesc(Integer estudianteId);
}