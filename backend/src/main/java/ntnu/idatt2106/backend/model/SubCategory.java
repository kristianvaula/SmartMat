package ntnu.idatt2106.backend.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.category.Category;


/**
 * Model class representing a SubCategory.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SubCategory")
@Schema(description = "A registered sub category of the application")
@Entity
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of the user, automatically generated")
    private long id;

    @Column(name = "name")
    @Schema(description = "The name of the user")
    private String name;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    @Schema(description = "The main category")
    private Category category;

    @Column(name = "categoryExpiryDays")
    @Schema(description = "Number of expected days to expiry")
    private int categoryExpiryDays;
}
