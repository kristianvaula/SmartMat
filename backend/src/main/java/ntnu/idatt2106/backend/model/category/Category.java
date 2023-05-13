package ntnu.idatt2106.backend.model.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class that represents a category
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
@Schema(description = "A registered category of the application")
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of the category, automatically generated")
    private long id;

    @Column(name = "name")
    @Schema(description = "The name of the category")
    private String name;
}
