package ntnu.idatt2106.backend.model.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for removing a member from a refrigerator. Force delete is a boolean which allows a request to force delete a refrigerator and its members.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoveMemberRequest {
    @NotNull
    private long refrigeratorId;
    @NotNull
    private String userName;
    private boolean forceDelete;

}
