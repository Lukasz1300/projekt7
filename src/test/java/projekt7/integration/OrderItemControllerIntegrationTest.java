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
import projekt7.entity.*;
import projekt7.repository.*;

import java.math.BigDecimal;
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
public class OrderItemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;


    @Autowired
    private UserRepository userRepository;

    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {
        restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setAddressCity("Warszawa");
        restaurant.setAddressStreet("Kwiatowa");
        restaurant.setPhoneNumber("+123456789");
        restaurant.setEmail("test@restaurant.com");
        restaurant.setDescription("Test restaurant");
        restaurant.setCreatedAt(LocalDateTime.now());
        restaurantRepository.save(restaurant);  // Zapisujemy restaurację

        User user = new User();
        user.setUsername("jane_doe");
        user.setPassword("password123");
        user.setEmail("jane.doe@example.com");
        userRepository.save(user);

        Order order = new Order();
        order.setUser(user); // Ustawiamy użytkownika
        order.setRestaurant(restaurant);
        order.setOrderStatus("PENDING");
        order.setTotalPrice(BigDecimal.valueOf(100.00));
        order.setAddressCity("City");
        order.setAddressStreet("Street");
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);

        MenuItem menuItem = new MenuItem();
        menuItem.setName("Test Menu Item");
        menuItem.setPrice(BigDecimal.valueOf(30.00));
        menuItem.setRestaurant(restaurant);
        menuItemRepository.save(menuItem);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(2);
        orderItem.setPrice(BigDecimal.valueOf(20.00));
        orderItemRepository.save(orderItem);
    }

    @Test
    public void testGetAllOrderItems() throws Exception {
        mockMvc.perform(get("/order-items/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderitems/list"))
                .andExpect(model().attributeExists("orderItems"))
                .andExpect(model().attribute("orderItems", hasSize(1)))
                .andExpect(model().attribute("orderItems", hasItem(
                        allOf(
                                hasProperty("quantity", is(2)),
                                hasProperty("price", is(BigDecimal.valueOf(20.00)))
                        )
                )));
    }

//    @Test
//    public void testGetOrderItemById() throws Exception {
//        OrderItem orderItem = orderItemRepository.findAll().get(0);
//
//        mockMvc.perform(get("/order-items/{id}", orderItem.getId()))
//                .andExpect(status().isOk())  // Sprawdzamy, że odpowiedź jest OK (status 200)
//                .andExpect(model().attributeExists("orderItem"))  // Sprawdzamy, że model zawiera atrybut "orderItem"
//                .andExpect(model().attribute("orderItem", hasProperty("quantity", is(2))));  // Sprawdzamy wartość "quantity" w atrybucie "orderItem"
//    }

//    @Test
//    public void testShowNewOrderItemForm() throws Exception {
//        mockMvc.perform(get("/order-items/form"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("orderitems/form"))
//                .andExpect(model().attributeExists("orderItem"));
//    }

    @Test
    public void testSaveOrderItem() throws Exception {
        Order order = orderRepository.findAll().get(0);
        MenuItem menuItem = menuItemRepository.findAll().get(0);

        // Submit the form to save a new OrderItem
        mockMvc.perform(post("/order-items/form") // Zmieniono adres na właściwy
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("orderId", order.getId().toString()) // Zmieniono parametr na zgodny z metodą
                        .param("menuItemIds", menuItem.getId().toString())
                        .param("quantities", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order-items/list"));

        // Verify the new order item is saved
        assertEquals(2, orderItemRepository.count()); // We expect 2 order items now
        OrderItem savedOrderItem = orderItemRepository.findAll().get(1); // Fetch the second item
        assertEquals(3, savedOrderItem.getQuantity()); // Check the quantity
      }


    @Test
    public void testDeleteOrderItem() throws Exception {
        OrderItem orderItem = orderItemRepository.findAll().get(0);

        // Wykonaj żądanie DELETE
        mockMvc.perform(delete("/order-items/delete/{id}", orderItem.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order-items/list"));
        // Sprawdź, czy element zamówienia został usunięty
        assertEquals(0, orderItemRepository.count());
    }
}
