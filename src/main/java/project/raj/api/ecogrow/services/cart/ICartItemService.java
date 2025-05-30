package project.raj.api.ecogrow.services.cart;

import project.raj.api.ecogrow.models.CartItem;

import java.util.Optional;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    Optional<CartItem> getCartItem(Long productId, Long cartId);
}
