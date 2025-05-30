package project.raj.api.ecogrow.services.cart;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.raj.api.ecogrow.exceptions.ResourceNotFoundException;
import project.raj.api.ecogrow.models.Cart;
import project.raj.api.ecogrow.models.CartItem;
import project.raj.api.ecogrow.models.Product;
import project.raj.api.ecogrow.repository.CartItemRepository;
import project.raj.api.ecogrow.repository.CartRepository;
import project.raj.api.ecogrow.services.product.ProductService;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{
    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final CartRepository cartRepository;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = getCartItem(productId, cartId).orElse(new CartItem());

        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() * quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(productId, cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));

        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems().stream().
                filter(item -> item.getProduct().getId().equals(productId)).findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                    cartItemRepository.save(item);
                });
        cart.updateTotalAmount();
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public Optional<CartItem> getCartItem(Long productId, Long cartId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
    }
}
