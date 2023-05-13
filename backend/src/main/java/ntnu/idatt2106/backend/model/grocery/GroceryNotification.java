package ntnu.idatt2106.backend.model.grocery;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import ntnu.idatt2106.backend.model.User;

/**
 * A notification regarding a grocery.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "GroceryNotification")
@Schema(description = "A notification regarding a grocery entity, for example a grocery entity is expiring soon")
@Entity
public class GroceryNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of a grocery notification")
    private long id;

    @ManyToOne
    @JoinColumn(name = "users")
    @Schema(description = "User which owns the notification")
    private User user;

    @ManyToOne
    @JoinColumn(name = "RefrigeratorGrocery")
    @Schema(description = "The grocery entity which the notification regards")
    private RefrigeratorGrocery groceryEntity;

    @Column(name = "daysLeft")
    @Schema(description = "The number of days left before the grocery expires")
    private Long daysLeft;

    @Column(name = "deleted")
    @Schema(description = "Shows if the user has deleted/dismissed the notification or not")
    private boolean deleted;

}
