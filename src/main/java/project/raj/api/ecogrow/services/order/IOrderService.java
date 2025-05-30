package project.raj.api.ecogrow.services.order;

import project.raj.api.ecogrow.dto.OrderDto;
import project.raj.api.ecogrow.models.Order;

import java.util.List;

public interface IOrderService {
    OrderDto placeOrder(Long userId);

    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrder(Long userId);

    OrderDto convertToOrderDto(Order order);
}
