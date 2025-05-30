package project.raj.api.ecogrow.response;

import lombok.AllArgsConstructor;
import lombok.Data;

// return data to frontend
@Data
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private Object data;
}
