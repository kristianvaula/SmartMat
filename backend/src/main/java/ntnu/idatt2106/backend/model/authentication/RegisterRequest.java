package ntnu.idatt2106.backend.model.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Class that represents a request for registering
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
}