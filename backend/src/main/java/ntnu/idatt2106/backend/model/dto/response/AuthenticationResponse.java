package ntnu.idatt2106.backend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class AuthenticationResponse {

    @JsonIgnore
    private String token;
    private String userId;
    private String userRole;

}
