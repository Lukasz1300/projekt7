package projekt7.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import projekt7.entity.Restaurant;
import projekt7.entity.RestaurantOwner;
import projekt7.repository.RestaurantOwnerRepository;
import projekt7.repository.RestaurantRepository;
import projekt7.service.RestaurantOwnerService;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = {"ADMIN"})
@ActiveProfiles("test")
public class RestaurantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RestaurantOwnerService restaurantOwnerService;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private RestaurantOwnerRepository restaurantOwnerRepository;
    private Long savedOwnerId;

    @BeforeEach
    public void setUp() {

        RestaurantOwner owner = new RestaurantOwner();
        owner.setFirstName("Jan");
        owner.setLastName("Kowalski");
        owner.setEmail("owner@test.com");
        owner.setPhoneNumber("+1234567890");
        restaurantOwnerRepository.save(owner);
        savedOwnerId = restaurantOwnerRepository.save(owner).getId();

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);
        restaurant.setName("Test Restaurant");
        restaurant.setAddressCity("Warszawa");
        restaurant.setAddressStreet("Kwiatowa");
        restaurant.setPhoneNumber("+1234567890");
        restaurant.setEmail("restaurant@test.com");
        restaurant.setDescription("Test Description");
        restaurant.setCreatedAt(LocalDateTime.now());
        restaurantRepository.save(restaurant);
    }

    @Test
    public void testGetAllRestaurants() throws Exception {
        mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants/list"))
                .andExpect(model().attributeExists("restaurants"))
                .andExpect(model().attribute("restaurants", hasSize(1)))
                .andExpect(model().attribute("restaurants", hasItem(
                        allOf(
                                hasProperty("name", is("Test Restaurant")),
                                hasProperty("addressCity", is("Warszawa")),
                                hasProperty("addressStreet", is("Kwiatowa"))
                        )
                )));
    }

    @Test
    public void testGetRestaurantById() throws Exception {
        Restaurant restaurant = restaurantRepository.findAll().get(0);

        mockMvc.perform(get("/restaurants/{id}", restaurant.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants/view"))
                .andExpect(model().attributeExists("restaurant"))
                .andExpect(model().attribute("restaurant", hasProperty("name", is("Test Restaurant"))));
    }

    @Test
    public void testGetRestaurantsByOwnerId() throws Exception {
        mockMvc.perform(get("/restaurants/owner/{ownerId}", savedOwnerId))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants/list"))
                .andExpect(model().attributeExists("restaurants"))
                .andExpect(model().attribute("restaurants", hasSize(1)))
                .andExpect(model().attribute("restaurants", hasItem(
                        allOf(
                                hasProperty("name", is("Test Restaurant")),
                                hasProperty("addressCity", is("Warszawa")),
                                hasProperty("addressStreet", is("Kwiatowa"))
                        )
                )));
    }

    @Test
    public void testShowNewRestaurantForm() throws Exception {
        mockMvc.perform(get("/restaurants/form/{id}" , savedOwnerId))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants/form"))
                .andExpect(model().attributeExists("restaurant"));
    }

    @Test
    public void testSaveRestaurant() throws Exception {
        RestaurantOwner owner = restaurantOwnerRepository.findAll().get(0);

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("owner.id", owner.getId().toString())
                        .param("name", "New Restaurant")
                        .param("addressCity", "Miasto") // Dodano cityName
                        .param("addressStreet", "45 New Street") // Dodano streetName
                        .param("phoneNumber", "+0987654321")
                        .param("email", "newrestaurant@test.com")
                        .param("description", "New Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/restaurants"));

        // Weryfikacja nowej restauracji
        Restaurant savedRestaurant = restaurantRepository.findAll().get(1); // Index 1 przy założeniu, że jest jedna istniejąca restauracja
        assertEquals("New Restaurant", savedRestaurant.getName());
        assertEquals("Miasto", savedRestaurant.getAddressCity());
        assertEquals("45 New Street", savedRestaurant.getAddressStreet());
    }


    @Test
    public void testDeleteRestaurant() throws Exception {
        Restaurant restaurant = restaurantRepository.findAll().get(0);

        mockMvc.perform(delete("/restaurants/delete/{id}", restaurant.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Dodaj CSRF, jeśli jest wymagany
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/restaurants"));
        // Verify the restaurant is deleted
        assertEquals(0, restaurantRepository.count());
    }
}



