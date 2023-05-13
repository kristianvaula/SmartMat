package ntnu.idatt2106.backend.model.dto.response;

import lombok.Data;

/**
 * Error response model.
 */
@Data
public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
