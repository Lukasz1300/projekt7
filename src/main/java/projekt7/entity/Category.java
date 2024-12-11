package projekt7.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_seq")
    @SequenceGenerator(name = "categories_seq", sequenceName = "categories_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Nazwa kategorii nie może być pusta")
    @Size(max = 50, message = "Nazwa kategorii nie może przekraczać 50 znaków")
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<MenuItemCategory> menuItemCategories = new HashSet<>();
}
