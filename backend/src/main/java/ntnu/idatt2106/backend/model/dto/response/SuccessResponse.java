package ntnu.idatt2106.backend.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO success response is used as response from endpoints when the feature is completed as expected
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse {


    private String message;
    private int statusCode;
}
