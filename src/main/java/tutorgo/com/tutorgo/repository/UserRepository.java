package tutorgo.com.tutorgo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tutorgo.com.tutorgo.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String phoneNumber);
}
