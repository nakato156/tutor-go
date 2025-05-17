package tutorgo.com.tutorgo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tutorgo.com.tutorgo.model.entity.Tutor;

import java.util.Optional;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Integer> {

    // Buscar por ID de usuario
    Optional<Tutor> findByUsuarioId(Integer usuarioId);

    // Buscar por email del usuario usando JPQL
    @Query("SELECT t FROM Tutor t JOIN t.usuario u WHERE u.email = :email")
    Optional<Tutor> findByUsuarioEmail(String email);

    // Listar todos los tutores con paginación
    Page<Tutor> findAll(Pageable pageable);
}
