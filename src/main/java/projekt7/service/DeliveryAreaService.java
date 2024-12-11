package projekt7.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekt7.entity.DeliveryArea;
import projekt7.repository.DeliveryAreaRepository;

import java.util.List;

@Transactional
@Service
@Slf4j
public class DeliveryAreaService {

    private final DeliveryAreaRepository deliveryAreaRepository;
    private final RestaurantService restaurantService;
    private final UserService userService;


    @Autowired
    public DeliveryAreaService(DeliveryAreaRepository deliveryAreaRepository, RestaurantService restaurantService, UserService userService) {
        this.deliveryAreaRepository = deliveryAreaRepository;
        this.restaurantService = restaurantService;
        this.userService = userService;
    }

    // Metoda do znalezienia obszarów dostawy według identyfikatora restauracji
    public List<DeliveryArea> findByRestaurantId(Long restaurantId) {
        return deliveryAreaRepository.findByRestaurantId(restaurantId);
    }

//    public boolean isDeliveryAvailableForRestaurant(Long restaurantId) {
//        // Pobierz restaurację
//        Optional<Restaurant> restaurant = restaurantService.findRestaurantById(restaurantId);
//        if (restaurant.isEmpty()) {
//            return false; // Jeśli restauracja nie istnieje
//        }
//        // Sprawdź, czy restauracja ma zdefiniowane obszary dostawy
//        for (DeliveryArea area : restaurant.get().getDeliveryAreas()) {
//            // Sprawdź, czy istnieje obszar dostawy
//            if (area != null) {
//                return true; // Jeśli znaleziono jakikolwiek obszar dostawy
//            }
//        }
//        return false; // Jeśli nie znaleziono obszaru dostawy
//    }

}
