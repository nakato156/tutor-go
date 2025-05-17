package tutorgo.com.tutorgo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tutorgo.com.tutorgo.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}
