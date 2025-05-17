package tutorgo.com.tutorgo.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tutorgo.com.tutorgo.model.entity.Resenias;

public interface ResenaRepository extends JpaRepository<Resenias, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Resenias r WHERE r.sesion.id = :sesionId")
    void deleteBySesionId(@Param("sesionId") Integer sesionId);
}
