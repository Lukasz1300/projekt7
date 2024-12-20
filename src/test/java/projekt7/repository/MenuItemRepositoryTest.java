package projekt7.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import projekt7.entity.MenuItem;
import projekt7.entity.Restaurant;
import projekt7.entity.RestaurantOwner;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class MenuItemRepositoryTest {

    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private RestaurantOwnerRepository restaurantOwnerRepository;

    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {
        // Wstaw przykładowego właściciela restauracji
        RestaurantOwner owner = new RestaurantOwner();
        owner.setFirstName("Jan");
        owner.setLastName("Kowalski");
        owner.setEmail("owner@test.com");
        owner.setPhoneNumber("+123456789");
        restaurantOwnerRepository.save(owner);

        restaurant = new Restaurant();
        restaurant.setOwner(owner);
        restaurant.setName("Test Restaurant");
        restaurant.setAddressStreet("Warszawa");
        restaurant.setAddressStreet("Kwiatowa");
        restaurantRepository.save(restaurant);

        MenuItem menuItem1 = new MenuItem();
        menuItem1.setRestaurant(restaurant);
        menuItem1.setName("Pizza");
        menuItem1.setPrice(BigDecimal.valueOf(10.99));
        menuItemRepository.save(menuItem1);

        MenuItem menuItem2 = new MenuItem();
        menuItem2.setRestaurant(restaurant);
        menuItem2.setName("Burger");
        menuItem2.setPrice(BigDecimal.valueOf(5.99));
        menuItemRepository.save(menuItem2);
    }

    @Test
    public void givenMenuItems_whenFindByRestaurantId_thenReturnMenuItems() {
        // When
        List<MenuItem> menuItems = menuItemRepository.findByRestaurantId(restaurant.getId());

        // Then
        assertThat(menuItems).hasSize(2);
        assertThat(menuItems).extracting(MenuItem::getName)
                .containsExactlyInAnyOrder("Pizza", "Burger");
        assertThat(menuItems).extracting(MenuItem::getRestaurant)
                .containsOnly(restaurant);
    }
}
