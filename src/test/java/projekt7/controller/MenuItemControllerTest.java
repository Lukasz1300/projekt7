package projekt7.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import projekt7.entity.MenuItem;
import projekt7.entity.Restaurant;
import projekt7.service.MenuItemService;
import projekt7.service.RestaurantService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(MenuItemController.class)
@WithMockUser(username = "testuser", roles = {"ADMIN"})
@ActiveProfiles("test")
public class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuItemService menuItemService;

    @MockBean
    private RestaurantService restaurantService;

    @Test
    public void shouldReturnAllMenuItems() throws Exception {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Sample Item");
        menuItem.setPrice(BigDecimal.valueOf(10.99));
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Sample Restaurant");
        menuItem.setRestaurant(restaurant);

        List<MenuItem> menuItems = Collections.singletonList(menuItem);
        // Mockowanie serwisu
        when(menuItemService.findAllMenuItems()).thenReturn(menuItems);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/menu-item")) // Poprawiony URL
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("menuitem/list"))
                .andExpect(MockMvcResultMatchers.model().attribute("menuItems", menuItems))
                .andDo(print());
    }

    @Test
    public void shouldReturnMenuItemsByRestaurantId() throws Exception {
        // Given
        Long restaurantId = 1L;
        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Sample Item");
        menuItem.setPrice(BigDecimal.valueOf(10.99));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setName("Sample Restaurant");
        menuItem.setRestaurant(restaurant);

        List<MenuItem> menuItems = Collections.singletonList(menuItem);
        when(menuItemService.findMenuItemsByRestaurantId(restaurantId)).thenReturn(menuItems);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/menu-item/restaurant/{restaurantId}",  restaurantId))  // Poprawiony URL z restaurantId
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("menuitem/list"))
                .andExpect(MockMvcResultMatchers.model().attribute("menuItems", menuItems))
                .andDo(print());
    }


    @Test
    public void shouldReturnMenuItemById() throws Exception {
        // Given
        Long id = 1L;
        MenuItem menuItem = new MenuItem();
        when(menuItemService.findMenuItemById(id)).thenReturn(Optional.of(menuItem));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/menu-item/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("menuitem/detail"))
                .andExpect(MockMvcResultMatchers.model().attribute("menuItem", menuItem))
                .andDo(print());
    }

    @Test
    public void shouldReturn404WhenMenuItemNotFound() throws Exception {
        // Given
        Long id = 1L;
        when(menuItemService.findMenuItemById(id)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/menu-item/" + id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.view().name("error/404"))
                .andDo(print());
    }
}

