package projekt7.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import projekt7.entity.*;
import projekt7.service.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(OrderController.class)
@ActiveProfiles("test")
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private OrderService orderService;
    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private DeliveryAreaService deliveryAreaService;
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private OrderItemService orderItemService;

    @Test
    @WithMockUser
    public void shouldReturnAllOrders() throws Exception {
        List<Order> orders = Collections.singletonList(new Order());
        when(orderService.findAllOrders()).thenReturn(orders);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("orders/list"))
                .andExpect(MockMvcResultMatchers.model().attribute("orders", orders))
                .andDo(print());
    }

//    @Test
//    @WithMockUser
//    public void shouldReturnOrderById() throws Exception {
//        Long id = 1L;
//        Order order = new Order();
//        when(orderService.findOrderById(id)).thenReturn(Optional.of(order));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/orders/" + id))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("orders/view"))
//                .andExpect(MockMvcResultMatchers.model().attribute("order", order))
//                .andDo(print());
//    }

    @Test
    @WithMockUser
    public void shouldReturn404WhenOrderNotFound() throws Exception {
        Long id = 1L;
        when(orderService.findOrderById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/" + id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.view().name("error/404"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void shouldReturnOrdersByUserId() throws Exception {
        Long userId = 1L;
        List<Order> orders = Collections.singletonList(new Order());
        when(orderService.findOrdersByUserId(userId)).thenReturn(orders);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/user/" + userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("orders/list"))
                .andExpect(MockMvcResultMatchers.model().attribute("orders", orders))
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void shouldReturnOrdersByRestaurantId() throws Exception {
        Long restaurantId = 1L;
        List<Order> orders = Collections.singletonList(new Order());
        when(orderService.findOrdersByRestaurantId(restaurantId)).thenReturn(orders);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/restaurant/" + restaurantId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("orders/list"))
                .andExpect(MockMvcResultMatchers.model().attribute("orders", orders))
                .andDo(print());
    }
//    @Test
//    @WithMockUser
//    public void shouldShowNewOrderForm() throws Exception {
//        // Tworzenie użytkownika
//        User user = new User();
//        user.setUsername("jane_doe");
//        user.setPassword("password123");
//        user.setEmail("jane.doe@example.com");
//
//        // Mockowanie zwracania listy użytkowników
//        when(userService.findAllUsers()).thenReturn(Arrays.asList(user));
//
//        // Tworzenie restauracji
//        Restaurant restaurant = new Restaurant();
//        restaurant.setName("Test Restaurant");
//        restaurant.setAddressCity("Warszawa");
//        restaurant.setAddressStreet("Kwiatowa");
//        restaurant.setPhoneNumber("+123456789");
//        restaurant.setEmail("restaurant@example.com");
//        restaurant.setCreatedAt(LocalDateTime.now());
//
//        // Mockowanie zwracania listy restauracji
//        when(restaurantService.findAllRestaurants()).thenReturn(Arrays.asList(restaurant));
//
//        // Tworzenie kategorii
//        Category category = new Category();
//        category.setName("Italian");
//
//        // Tworzenie pozycji menu
//        MenuItem menuItem = new MenuItem();
//        menuItem.setId(1L);
//        menuItem.setName("Pizza Margherita");
//
//// Konwersja List na Set
//        category.setMenuItems(new HashSet<>(Arrays.asList(menuItem)));
//
//        // Mockowanie zwracania listy kategorii
//        when(categoryService.findAllCategories()).thenReturn(Arrays.asList(category));
//
//        // Przeprowadzenie testu
//        mockMvc.perform(MockMvcRequestBuilders.get("/orders/form"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("orders/form"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("order"))
//                .andExpect(MockMvcResultMatchers.model().attribute("users", hasSize(1))) // Sprawdzamy, że jest jeden użytkownik
//                .andExpect(MockMvcResultMatchers.model().attribute("restaurants", hasSize(1))) // Sprawdzamy, że jest jedna restauracja
//                .andExpect(MockMvcResultMatchers.model().attribute("categories", hasSize(1))) // Sprawdzamy, że jest jedna kategoria
//                .andDo(print());
//    }



    @Test
    @WithMockUser
    public void shouldSaveOrder() throws Exception {
        // Przygotowanie danych
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setAddressCity("Katowice");
        restaurant.setAddressStreet("Leśna");
        restaurant.setPhoneNumber("123456789");
        restaurant.setEmail("test@example.com");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setEmail("testuser@example.com");
        user.setAddressCity("Katowice");
        user.setAddressStreet("Leśna");  // Adres użytkownika zgodny z adresem restauracji

        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setOrderStatus("PENDING");
 //       order.setTotalPrice(new BigDecimal("100.00"));
        order.setOrderDate(LocalDateTime.now());


        // Mockowanie metod serwisowych
        when(userService.findUserById(user.getId())).thenReturn(Optional.of(user));
        when(restaurantService.findRestaurantById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(orderService.saveOrder(any(Order.class))).thenReturn(order); // Użycie any(Order.class) dla większej elastyczności

        // Wykonanie testu
        mockMvc.perform(MockMvcRequestBuilders.post("/orders/form")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .flashAttr("order", order))  // Przekazanie zamówienia w atrybucie
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())  // Oczekiwany status 3xx (przekierowanie)
                .andExpect(MockMvcResultMatchers.redirectedUrl("/orders"))  // Sprawdzenie przekierowania na listę zamówień
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void shouldDeleteOrder() throws Exception {

        long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/delete/" + id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Dodanie tokena CSRF
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/orders"))
                .andDo(print());
    }
}
