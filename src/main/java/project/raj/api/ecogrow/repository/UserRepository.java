package project.raj.api.ecogrow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.raj.api.ecogrow.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User getUserById(Long id);

    User findByEmail(String email);

}
