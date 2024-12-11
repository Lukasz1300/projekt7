package projekt7.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.HashSet;
import java.util.Set;

@Data
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "restaurant_owners")
public class RestaurantOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Imię jest wymagane")
    @Size(max = 100, message = "Imię nie może przekraczać 100 znaków")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank(message = "Nazwisko jest wymagane")
    @Size(max = 100, message = "Nazwisko nie może przekraczać 100 znaków")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Email powinien być prawidłowy")
    @Size(max = 100, message = "Email nie może przekraczać 100 znaków")
    @Column(nullable = false, length = 100)
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Numer telefonu musi zawierać tylko cyfry i opcjonalny znak '+' na początku, oraz mieć maksymalnie 15 cyfr")
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Restaurant> restaurants = new HashSet<>();

}


