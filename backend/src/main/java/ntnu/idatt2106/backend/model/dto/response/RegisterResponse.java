package ntnu.idatt2106.backend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Class that represents a response for authentication
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    private String userId;
    private String userRole;
    private String message;

}
