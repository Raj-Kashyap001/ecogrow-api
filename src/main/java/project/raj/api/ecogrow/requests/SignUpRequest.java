package project.raj.api.ecogrow.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import project.raj.api.ecogrow.validation.ValidPassword;

@Data
public class SignUpRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required") // TODO: Validate Email like password
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;

}
