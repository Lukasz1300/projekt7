package projekt7.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt7.entity.Restaurant;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByOwnerId(Long id);

   }
