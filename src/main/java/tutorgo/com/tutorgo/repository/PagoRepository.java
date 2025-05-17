package tutorgo.com.tutorgo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tutorgo.com.tutorgo.model.entity.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    // Buscar pagos realizados por un estudiante
    @Query("SELECT p FROM Pago p WHERE p.estudiante.id = :estudianteId ORDER BY p.fecha DESC")
    List<Pago> findPagosByEstudianteId(Integer estudianteId);

    // Buscar pagos recibidos por un tutor
    @Query("SELECT p FROM Pago p WHERE p.tutor.id = :tutorId ORDER BY p.fecha DESC")
    List<Pago> findPagosByTutorId(Integer tutorId);

    // Verificar si existe alguna transacción para un estudiante o tutor
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Pago p WHERE p.estudiante.id = :usuarioId OR p.tutor.id = :usuarioId")
    boolean existsTransaccionesByUsuarioId(Integer usuarioId);
}
