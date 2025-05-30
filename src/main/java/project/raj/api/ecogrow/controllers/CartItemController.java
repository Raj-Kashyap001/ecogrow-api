package project.raj.api.ecogrow.controllers;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.raj.api.ecogrow.exceptions.ResourceNotFoundException;
import project.raj.api.ecogrow.models.Cart;
import project.raj.api.ecogrow.models.User;
import project.raj.api.ecogrow.response.ApiResponse;
import project.raj.api.ecogrow.services.cart.ICartItemService;
import project.raj.api.ecogrow.services.cart.ICartService;
import project.raj.api.ecogrow.services.user.UserService;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final UserService userService;
    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart (
            @RequestParam Long productId,
            @RequestParam int quantity) {
        try {
                User user = userService.getAuthenticatedUser();
                Cart cart = cartService.initializeCart(user);
            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Added to card!", null));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (JwtException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/item/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart (
            @RequestParam  Long cartId,
            @RequestParam Long productId) {
        try {
            //TODO: Check for bad params
           // if (cartId != null && productId != null) return ResponseEntity.badRequest().body(new ApiResponse("Invalid Request Parameters!", null));
            cartItemService.removeItemFromCart(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("success", null));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/item/updateQuantity")
    public ResponseEntity<ApiResponse> updateCartItemQuantity (
            @RequestParam Long cartId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
       try {
           cartItemService.updateItemQuantity(cartId, productId, quantity);
           return ResponseEntity.ok(new ApiResponse("update success", null));
       } catch (ResourceNotFoundException e) {
           return  ResponseEntity.status(NOT_FOUND)
                   .body(new ApiResponse(e.getMessage(), null));
       }
    }
}
