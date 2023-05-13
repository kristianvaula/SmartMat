package ntnu.idatt2106.backend.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.Refrigerator;

import java.util.List;

/**
 * Model for creating refrigerator response to frontend.
 * Provides all information about a refrigerator and its users.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefrigeratorDTO {
    private long id;
    @NotNull
    private String name;
    private String address;
    private List<MemberDTO> members;

    public RefrigeratorDTO(Refrigerator refrigerator, List<MemberDTO> members) {
        this.id = refrigerator.getId();
        this.name = refrigerator.getName();
        this.address = refrigerator.getAddress();
        this.members = members;
    }
}
