package ntnu.idatt2106.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.RefrigeratorUser;
import ntnu.idatt2106.backend.model.enums.FridgeRole;

/**
 * Model class used to return public information about a member in a refrigerator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private long refrigeratorId;
    private String name;
    private String username;
    private FridgeRole fridgeRole;

    public MemberDTO(RefrigeratorUser ru) {
        this.refrigeratorId = ru.getRefrigerator().getId();
        this.name = ru.getUser().getName();
        this.username = ru.getUser().getUsername();
        this.fridgeRole = ru.getFridgeRole();
    }
}
