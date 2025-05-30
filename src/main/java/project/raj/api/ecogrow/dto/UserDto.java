package project.raj.api.ecogrow.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders;
    private CartDto cart;
}
