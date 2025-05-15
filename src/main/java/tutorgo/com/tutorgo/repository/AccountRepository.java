package tutorgo.com.tutorgo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tutorgo.com.tutorgo.model.Account;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
}
