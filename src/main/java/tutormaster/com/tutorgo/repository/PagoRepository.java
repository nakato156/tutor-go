package tutormaster.com.tutorgo.repository;

import tutormaster.com.tutorgo.model.entity.Pago; // Asegúrate que el import sea correcto
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
    // Ordenado por ID descendente (asumiendo que los más recientes tienen ID mayor)
    @Query("SELECT p FROM Pago p WHERE p.tutor.id = :tutorId ORDER BY p.id DESC")
    List<Pago> findByTutorIdOrderByIdDesc(Integer tutorId); // Nombre del método actualizado

    // Ordenado por ID descendente
    @Query("SELECT p FROM Pago p WHERE p.estudiante.id = :estudianteId ORDER BY p.id DESC")
    List<Pago> findByEstudianteIdOrderByIdDesc(Integer estudianteId); // Nombre del método actualizado
}