package project.raj.api.ecogrow.services.cart;

import project.raj.api.ecogrow.models.Cart;
import project.raj.api.ecogrow.models.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    Cart initializeCart(User user);

    Cart getCartByUserId(Long userId);
}
