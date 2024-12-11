package projekt7.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt7.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Niestandardowe metody do znalezienia zamówień według identyfikatora użytkownika i restauracji
    List<Order> findByUserId(Long userId);
    List<Order> findByRestaurantId(Long restaurantId);
}

