package tutorgo.com.tutorgo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tutorgo.com.tutorgo.model.entity.Tutor;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Integer> {
    Page<Tutor> findAll(Pageable pageable);
}
