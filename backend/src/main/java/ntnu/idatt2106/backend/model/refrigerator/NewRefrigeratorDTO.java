package ntnu.idatt2106.backend.model.refrigerator;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for when a new Refrigerator object is requested from frontend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewRefrigeratorDTO {

    private String userEmail;
    private String name;
    private String address;
}
