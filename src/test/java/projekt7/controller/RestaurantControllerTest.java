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
import projekt7.entity.Restaurant;
import projekt7.entity.RestaurantOwner;
import projekt7.service.RestaurantOwnerService;
import projekt7.service.RestaurantService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantOwnerService restaurantOwnerService;

    @MockBean
    private RestaurantService restaurantService;

    @Test
    @WithMockUser
    public void shouldReturnAllRestaurants() throws Exception {
        // Given
        List<Restaurant> restaurants = Collections.singletonList(new Restaurant());
        when(restaurantService.findAllRestaurants()).thenReturn(restaurants);

        // When & Then
        mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants/list"))
                .andExpect(model().attribute("restaurants", restaurants));
    }

    @Test
    @WithMockUser
    public void shouldShowNewRestaurantForm() throws Exception {
        Long id = 1L;
        RestaurantOwner owner = new RestaurantOwner();
        owner.setFirstName("Jan");
        owner.setLastName("Kowalski");
        owner.setEmail("owner@example.com");
        owner.setPhoneNumber("+123456789");

        mockMvc.perform(get("/restaurants/form/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants/form")); // Zmieniono na poprawną nazwę widoku
    }

    @Test
    @WithMockUser
    public void shouldSaveRestaurant() throws Exception {
        // Given
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setAddressStreet("Warszawa");
        restaurant.setAddressStreet("Kwiatowa");
        restaurant.setPhoneNumber("123456789");
        restaurant.setEmail("test@example.com");
        restaurant.setDescription("A test restaurant.");
        when(restaurantService.saveRestaurant(restaurant)).thenReturn(restaurant);

          // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants")
                        .with(csrf()) //  uwzględnić CSRF token
                        .flashAttr("restaurant", restaurant))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/restaurants"));
    }

    @Test
    @WithMockUser
    public void shouldReturnRestaurantById() throws Exception {
        // Given
        Long id = 1L;

        RestaurantOwner owner = new RestaurantOwner();
        owner.setFirstName("Jan");
        owner.setLastName("Kowalski");
        owner.setEmail("owner@example.com");
        owner.setPhoneNumber("+123456789");

        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        restaurant.setOwner(owner);

        when(restaurantService.findRestaurantById(id)).thenReturn(Optional.of(restaurant));

        // When & Then
        mockMvc.perform(get("/restaurants/" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants/view"))
                .andExpect(model().attribute("restaurant", restaurant));
    }

    @Test
    @WithMockUser
    public void shouldReturnNotFoundWhenRestaurantDoesNotExist() throws Exception {
        // Given
        Long id = 1L;
        when(restaurantService.findRestaurantById(id)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/restaurants/" + id))
                .andExpect(status().isNotFound()) // Poprawka statusu na 404
                .andExpect(view().name("error/404")); // Sprawdzenie widoku błędu
    }

    @Test
    @WithMockUser
    public void shouldDeleteRestaurantById() throws Exception {
        // Given
        Long id = 1L;
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/delete/" + id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/restaurants"));
    }
}

