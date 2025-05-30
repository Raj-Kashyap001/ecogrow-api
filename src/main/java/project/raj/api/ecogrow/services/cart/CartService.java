package project.raj.api.ecogrow.services.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.raj.api.ecogrow.exceptions.ResourceNotFoundException;
import project.raj.api.ecogrow.models.Cart;
import project.raj.api.ecogrow.models.User;
import project.raj.api.ecogrow.repository.CartItemRepository;
import project.raj.api.ecogrow.repository.CartRepository;
import project.raj.api.ecogrow.services.user.UserService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{

    public final CartRepository cartRepository;
    public final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);
    private final UserService userService;

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Cart not found!"));
        System.out.println(cart.toString());
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCart(cart);
        cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }


    @Override
    public Cart initializeCart(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });

    }

    @Override
    public Cart getCartByUserId(Long userId) {
        if (userService.getUserById(userId) == null) throw new ResourceNotFoundException("User not found!");
        return cartRepository.findByUserId(userId);
    }
}
