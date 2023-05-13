package ntnu.idatt2106.backend.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object returned when user changes password.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDTO {

    private String oldPassword;
    private String newPassword;
}
