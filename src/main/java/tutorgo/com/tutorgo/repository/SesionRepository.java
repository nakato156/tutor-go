package tutorgo.com.tutorgo.repository;

import jakarta.transaction.Transactional;          // o javax.transaction.Transactional si usas esa versión
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tutorgo.com.tutorgo.model.entity.Sesion;

public interface SesionRepository extends JpaRepository<Sesion, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Sesion s WHERE s.estudiante.id = :estudianteId")
    void deleteByEstudianteId(@Param("estudianteId") Integer estudianteId);

}
