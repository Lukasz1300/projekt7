package projekt7.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import projekt7.entity.Restaurant;
import projekt7.entity.RestaurantOwner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantOwnerRepository restaurantOwnerRepository;

    private RestaurantOwner owner;
    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {

        owner = new RestaurantOwner();
        owner.setFirstName("Jan");
        owner.setLastName("Kowalski");
        owner.setEmail("jan@example.com");
        owner.setPhoneNumber("+11234567890");
        owner = restaurantOwnerRepository.save(owner);

        restaurant = new Restaurant();
        restaurant.setOwner(owner);
        restaurant.setName("The Gourmet Bistro");
        restaurant.setAddressCity("Warszawa");
        restaurant.setAddressStreet("Kwiatowa");
        restaurant.setPhoneNumber("+11234567890");
        restaurant.setEmail("info@gourmetbistro.com");
        restaurant.setDescription("A fine dining experience.");
        restaurant.setCreatedAt(LocalDateTime.now());
        restaurant = restaurantRepository.save(restaurant);
    }

    @Test
    public void givenRestaurant_whenSaved_thenCanBeFoundById() {
        // When
        Optional<Restaurant> foundRestaurant = restaurantRepository.findById(restaurant.getId());

        // Then
        assertThat(foundRestaurant).isPresent();
        assertThat(foundRestaurant.get().getName()).isEqualTo("The Gourmet Bistro");
        assertThat(foundRestaurant.get().getAddressCity()).isEqualTo("Warszawa");
        assertThat(foundRestaurant.get().getAddressStreet()).isEqualTo("Kwiatowa");
        assertThat(foundRestaurant.get().getPhoneNumber()).isEqualTo("+11234567890");
        assertThat(foundRestaurant.get().getEmail()).isEqualTo("info@gourmetbistro.com");
        assertThat(foundRestaurant.get().getOwner()).isEqualTo(owner);
    }

    @Test
    public void givenRestaurant_whenDeleted_thenShouldNotBeFound() {
        // Given
        Long restaurantId = restaurant.getId();

        // When
        restaurantRepository.deleteById(restaurantId);
        Optional<Restaurant> foundRestaurant = restaurantRepository.findById(restaurantId);

        // Then
        assertThat(foundRestaurant).isNotPresent();
    }
}
