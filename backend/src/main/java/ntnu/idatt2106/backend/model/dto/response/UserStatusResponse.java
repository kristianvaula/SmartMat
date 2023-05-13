package ntnu.idatt2106.backend.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.enums.AuthenticationState;

/**
 * DTO for user status, contains information about hte authentication state, role and favorite refrigerator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatusResponse {

    private String userId;
    private AuthenticationState state;
    private String role;
    private Long favoriteRefrigeratorId;
}
