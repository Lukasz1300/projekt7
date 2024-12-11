package projekt7.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import projekt7.entity.Order;
import projekt7.entity.Restaurant;
import projekt7.entity.User;
import projekt7.repository.OrderRepository;
import projekt7.repository.RestaurantRepository;
import projekt7.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = {"ADMIN"})
@ActiveProfiles("test")
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    private Restaurant restaurant;
    private User user;
    private Order order;

    @BeforeEach
    public void setUp() {
        // Tworzenie restauracji
        restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setAddressCity("Warszawa");
        restaurant.setAddressStreet("Kwiatowa");
        restaurant.setPhoneNumber("+123456789");
        restaurant.setEmail("restaurant@example.com");
        restaurant.setCreatedAt(LocalDateTime.now());
        restaurant = restaurantRepository.save(restaurant);

        // Tworzenie użytkownika
        user = new User();
        user.setUsername("jane_doe");
        user.setPassword("password123");
        user.setEmail("jane.doe@example.com");
        userRepository.save(user);

        // Tworzenie zamówienia
        order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setOrderStatus("PENDING");
        order.setTotalPrice(BigDecimal.valueOf(100.00));
        order.setOrderDate(LocalDateTime.now());
        order.setAddressCity("Test City");
        order.setAddressStreet("Test Street");
        order.setOrderItems(new HashSet<>());
        orderRepository.save(order);
    }

    @Test
    public void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/list"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", hasSize(1)))
                .andExpect(model().attribute("orders", hasItem(
                        allOf(
                                hasProperty("orderStatus", is("PENDING")),
                                hasProperty("totalPrice", is(BigDecimal.valueOf(100.00)))
                        )
                )));
    }

    @Test
    public void testGetOrdersByUserId() throws Exception {
        User user = userRepository.findAll().get(0);

        mockMvc.perform(get("/orders/user/{userId}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/list"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", hasSize(1)))
                .andExpect(model().attribute("orders", hasItem(
                        hasProperty("orderStatus", is("PENDING"))
                )));
    }

    @Test
    public void testGetOrdersByRestaurantId() throws Exception {
        Restaurant restaurant = restaurantRepository.findAll().get(0);

        mockMvc.perform(get("/orders/restaurant/{restaurantId}", restaurant.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/list"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", hasSize(1)))
                .andExpect(model().attribute("orders", hasItem(
                        hasProperty("orderStatus", is("PENDING"))
                )));
    }

//    @Test
//    public void testShowNewOrderForm() throws Exception {
//        mockMvc.perform(get("/orders/form"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("orders/form"))
//                .andExpect(model().attributeExists("order", "users", "restaurants", "categories"));
//    }
//
//    @Test
//    public void testSaveOrderWithNullUserCity() throws Exception {
//        // Przygotowanie testu, gdzie miasto użytkownika jest null
//        user.setAddressCity(null);  // Ustawienie null w adresie użytkownika
//
//        mockMvc.perform(post("/orders")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//                        .param("user.id", user.getId().toString())
//                        .param("restaurant.id", restaurant.getId().toString())
//                        .param("orderStatus", "COMPLETED")
//                        .param("totalPrice", "150.00")
//                        .param("deliveryAddress", "Some Street"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("error"))  // Oczekujemy, że w modelu będzie błąd
//                .andExpect(model().attribute("error", "Conflict: The user's address city does not match the restaurant's city. Delivery not available. Cities must be the same."))
//                .andExpect(view().name("orders/form"));
//    }

    @Test
    public void testDeleteOrder() throws Exception {
        Order order = orderRepository.findAll().get(0);

        mockMvc.perform(delete("/orders/delete/{id}", order.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        // Verify the order is deleted
        assertEquals(0, orderRepository.count());
    }
}
