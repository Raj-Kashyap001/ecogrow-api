package project.raj.api.ecogrow.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.raj.api.ecogrow.dto.CartDto;
import project.raj.api.ecogrow.exceptions.ResourceNotFoundException;
import project.raj.api.ecogrow.models.Cart;
import project.raj.api.ecogrow.response.ApiResponse;
import project.raj.api.ecogrow.services.cart.ICartService;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts") // TODO: remove item from cart which is ordered.
public class CartController {
    private final ICartService cartService;
    private final ModelMapper modelMapper;


    @GetMapping("/cart/{cartId}")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
        try {
            Cart cart = cartService.getCart(cartId);
            CartDto cartDto = modelMapper.map(cart, CartDto.class);
            return ResponseEntity.ok(new ApiResponse("success", cartDto));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/total/{cartId}")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
        try {
            BigDecimal totalAmount = cartService.getTotalPrice(cartId);
            return ResponseEntity.ok(new ApiResponse("Total Price: ", totalAmount));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Transactional
    @DeleteMapping("/clear/{cartId}")
    public  ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.ok(new ApiResponse("Cart Cleared", null));
    }

}
