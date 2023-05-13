package ntnu.idatt2106.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.enums.UserRole;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


/**
 * Class representing a User.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Schema(description = "A registered user of the application")
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(255)")
    @Schema(description = "The id of the user, automatically generated")
    private String id;


    @Column(name = "name")
    @Schema(description = "The name of the user")
    private String name;

    @Column(name = "email", unique = true)
    @Schema(description = "The email of the user")
    private String email;


    @Column(name = "password")
    @Schema(description = "The password of the user")
    private String password;

    @Column(name = "user_role")
    @Schema(description = "The role of the user")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "favorite_refrigeratorId")
    @Schema(description = "The id of a optional favorite refrigerator")
    private Long favoriteRefrigeratorId;

    /**
     * Returns the granted authorities a user has.
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    /**
     * Returns the password of a user.
     * @return
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Returns the username of a user.
     * @return
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Returns true
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Returns true
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Returns true
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Returns true.
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
