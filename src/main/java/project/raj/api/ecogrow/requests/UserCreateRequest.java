package project.raj.api.ecogrow.requests;

import lombok.Data;

@Data
public class UserCreateRequest {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
