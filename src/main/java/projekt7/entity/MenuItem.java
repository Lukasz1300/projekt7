package projekt7.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_item_seq")
    @SequenceGenerator(name = "menu_item_seq", sequenceName = "menu_item_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull(message = "Restauracja nie może być pusta")
    private Restaurant restaurant;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Nazwa pozycji menu jest wymagana")
    @Size(max = 100, message = "Nazwa pozycji menu nie może przekraczać 100 znaków")
    private String name;

    @Column(columnDefinition = "TEXT")
    @Size(max = 2000, message = "Opis nie może przekraczać 2000 znaków")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Cena jest wymagana")
    @Positive(message = "Cena musi być większa niż zero")
    private BigDecimal price;

    @Column(name = "image_url", length = 255)
    @Size(max = 255, message = "Adres URL obrazu nie może przekraczać 255 znaków")
    private String imageUrl;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MenuItemCategory> menuItemCategories = new HashSet<>();

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    public void setSuma(BigDecimal sum) {
        
    }
}
