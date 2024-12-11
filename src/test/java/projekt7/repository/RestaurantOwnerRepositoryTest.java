package projekt7.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import projekt7.entity.RestaurantOwner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
//@TestPropertySource(locations = "classpath:application-test.yml")
public class RestaurantOwnerRepositoryTest {

    @Autowired
    private RestaurantOwnerRepository restaurantOwnerRepository;

    private RestaurantOwner restaurantOwner;

    @BeforeEach
    public void setUp() {
        // Wstaw przykładowego właściciela restauracji
        restaurantOwner = new RestaurantOwner();
        restaurantOwner.setFirstName("Jan");
        restaurantOwner.setLastName("Kowalski");
        restaurantOwner.setEmail("jan@example.com");
        restaurantOwner.setPhoneNumber("+1234567890");
        restaurantOwner = restaurantOwnerRepository.save(restaurantOwner);
    }

    @Test
    public void givenRestaurantOwner_whenSaved_thenCanBeFoundById() {
        // When
        Optional<RestaurantOwner> foundOwner = restaurantOwnerRepository.findById(restaurantOwner.getId());

        // Then
        assertThat(foundOwner).isPresent();
        assertThat(foundOwner.get().getFirstName()).isEqualTo("Jan");
        assertThat(foundOwner.get().getEmail()).isEqualTo("jan@example.com");
        assertThat(foundOwner.get().getPhoneNumber()).isEqualTo("+1234567890");
    }

    @Test
    public void givenRestaurantOwner_whenDeleted_thenShouldNotBeFound() {
        // Given
        Long ownerId = restaurantOwner.getId();

        // When
        restaurantOwnerRepository.deleteById(ownerId);
        Optional<RestaurantOwner> foundOwner = restaurantOwnerRepository.findById(ownerId);

        // Then
        assertThat(foundOwner).isNotPresent();
    }
}
