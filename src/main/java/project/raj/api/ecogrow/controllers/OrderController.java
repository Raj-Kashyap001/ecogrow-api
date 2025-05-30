package project.raj.api.ecogrow.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import project.raj.api.ecogrow.dto.OrderDto;
import project.raj.api.ecogrow.exceptions.ResourceNotFoundException;
import project.raj.api.ecogrow.response.ApiResponse;
import project.raj.api.ecogrow.services.order.IOrderService;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

// TODO: Implement virtual delivery functionality and change order status according to it.

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
       try {
           OrderDto order = orderService.getOrder(orderId);
           return ResponseEntity.ok(new ApiResponse("Found: ", order));
       } catch (ResourceNotFoundException e) {
           return ResponseEntity.status(NOT_FOUND)
                   .body(new ApiResponse("Order Not Found", null));
       }
    }

    @GetMapping("/order/user/{userId}")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        try {
            List<OrderDto> orders = orderService.getUserOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Found: ", orders));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Orders Not Found", null));
        }
    }


   // TODO: Add a controller for canceling order



    @Transactional
    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
        // TODO: handle when cart is empty or when order is placed remove items from cart
        try {
            OrderDto order = orderService.placeOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Order Placed!", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

}
