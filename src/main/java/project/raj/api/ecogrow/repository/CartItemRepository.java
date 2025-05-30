package project.raj.api.ecogrow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.raj.api.ecogrow.models.Cart;
import project.raj.api.ecogrow.models.CartItem;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCart(Cart cart);
}
