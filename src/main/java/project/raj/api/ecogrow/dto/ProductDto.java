package project.raj.api.ecogrow.dto;

import lombok.Data;
import project.raj.api.ecogrow.models.Category;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {private Long id;
    private String name;
    private String description;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private Category category;
    private List<ImageDto> images;
}
