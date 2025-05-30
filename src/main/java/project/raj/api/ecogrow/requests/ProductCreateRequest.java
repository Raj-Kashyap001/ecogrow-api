package project.raj.api.ecogrow.requests;

import lombok.Data;
import project.raj.api.ecogrow.models.Category;

import java.math.BigDecimal;

@Data
public class ProductCreateRequest {
    private Long id;
    private String name;
    private String description;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private Category category;
}
