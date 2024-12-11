package projekt7.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurants")
public class Restaurant {

//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_seq")
//    @SequenceGenerator(name = "restaurant_seq", sequenceName = "restaurant_seq", allocationSize = 1)
//    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "owner_id", nullable = true) // Właściciel jest opcjonalny
    private RestaurantOwner owner;

    @NotBlank(message = "Nazwa jest wymagana")
    @Size(max = 100, message = "Nazwa nie może przekraczać 100 znaków")
    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = " address_city", length = 255)
    @NotBlank(message = "Nazwa miasta jest wymagana")
    @Size(max = 255, message = "Nazwa miasta nie może przekraczać 255 znaków")
    private String addressCity;

    @Column(name = "address_street", length = 255)
    @NotBlank(message = "Nazwa ulicy jest wymagana")
    @Size(max = 255, message = "Nazwa ulicy nie może przekraczać 255 znaków")
    private String addressStreet;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Numer telefonu musi zawierać tylko cyfry i opcjonalny znak '+' na początku, oraz mieć maksymalnie 15 cyfr")
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Email(message = "Email powinien być prawidłowy")
    @Size(max = 100, message = "Email nie może przekraczać 100 znaków")
    @Column(length = 100)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MenuItem> menuItems = new HashSet<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DeliveryArea> deliveryAreas = new HashSet<>();

    @Override
    public String toString() {
        return "Restaurant{" +
                "menuItems=" + menuItems +
                '}';
    }
}
