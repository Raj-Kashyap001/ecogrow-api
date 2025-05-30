package project.raj.api.ecogrow.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

@Data
public class CartDto {
    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private Collection<CartItemDto> items = new HashSet<>();
}

