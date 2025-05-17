package tutorgo.com.tutorgo.repository;
import tutorgo.com.tutorgo.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findUsuarioById(Integer id);
}