package project.raj.api.ecogrow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.raj.api.ecogrow.models.Cart;
import project.raj.api.ecogrow.models.User;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> user(User user);

    Cart findByUserId(Long userId);
}
