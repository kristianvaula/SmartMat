package ntnu.idatt2106.backend.model.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.enums.FridgeRole;

/**
 * Model used to receive a request for adding or editing a member
 * of a refrigerator.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequest {
    @NotNull
    private long refrigeratorId;
    @NotNull
    private String userName;
    private FridgeRole fridgeRole;
}
