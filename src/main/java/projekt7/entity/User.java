package projekt7.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    @Size(max = 50, message = "Nazwa użytkownika nie może przekraczać 50 znaków")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "Hasło jest wymagane")
    @Size(min = 6, max = 100, message = "Hasło musi mieć od 6 do 100 znaków")
    @Column(nullable = false, length = 100)
    private String password;

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Email powinien być prawidłowy")
    @Size(max = 100, message = "Email nie może przekraczać 100 znaków")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Size(max = 50, message = "Imię nie może przekraczać 50 znaków")
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50, message = "Nazwisko nie może przekraczać 50 znaków")
    @Column(name = "last_name", length = 50)
    private String lastName;

    @NotBlank(message = "Numer telefonu nie może być pusty")
    @Pattern(regexp = "^\\+?[0-9]{1,15}$", message = "Numer telefonu musi zawierać tylko cyfry i opcjonalny znak '+' na początku, oraz mieć maksymalnie 15 cyfr")
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @NotBlank(message = "nie może być null")
    @Size(max = 50, message = "Miasto nie może przekraczać 50 znaków")
    @Column(name = "address_city", length = 50)
    private String addressCity;

    @NotBlank(message = "nie może być null")
    @Size(max = 50, message = "Ulica nie może przekraczać 50 znaków")
    @Column(name = "address_street", length = 50)
    private String addressStreet;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Zignoruj odniesienie do orders podczas serializacji
    private Set<Order> orders = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) role::getAuthority)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
