package projekt7.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User cannot be null")
    @JsonIgnore // Zignoruj odniesienie do user podczas serializacji
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull(message = "Restaurant cannot be null")
    private Restaurant restaurant;

    @Column(name = "order_status", length = 50)
    @NotBlank(message = "Order status cannot be blank")
    @Size(max = 50, message = "Order status cannot exceed 50 characters")
    private String orderStatus = "PENDING";

    @Column(name = "order_date")
    @NotNull(message = "Order date cannot be null")
    @PastOrPresent(message = "Order date cannot be in the future")
    private LocalDateTime orderDate = LocalDateTime.now();

    @NotNull(message = "Order City cannot be null")
    @Size(max = 50, message = "Miasto nie może przekraczać 50 znaków")
    @Column(name = "address_city", length = 50)
    private String addressCity;

    @NotNull(message = "Order Street cannot be null")
    @Size(max = 50, message = "Ulica nie może przekraczać 50 znaków")
    @Column(name = "address_street", length = 50)
    private String addressStreet;

    @Column(name = "total_price", precision = 10, scale = 2)
    @NotNull(message = "Total price cannot be null")
    @Positive(message = "Total price must be positive")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id != null && id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
