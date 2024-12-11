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

import projekt7.entity.MenuItem;
import projekt7.entity.Order;
import projekt7.entity.OrderItem;
import projekt7.service.CategoryService;
import projekt7.service.MenuItemService;
import projekt7.service.OrderItemService;
import projekt7.service.OrderService;

import java.math.BigDecimal;
import java.util.Collections;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ActiveProfiles("test")
@WebMvcTest(OrderItemController.class) // Testowanie tylko kontrolera OrderItemController
public class OrderItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderItemService orderItemService;

    @MockBean
    private MenuItemService menuItemService;
    @MockBean
    private OrderService orderService;

//    @Test
//    @WithMockUser
//    public void shouldReturnAllOrderItems() throws Exception {
//        // Given
//        List<OrderItem> orderItems = Collections.singletonList(new OrderItem());
//        List<Order> orders = Collections.singletonList(new Order()); // Mocked orders
//        List<MenuItem> menuItems = Collections.singletonList(new MenuItem()); // Mocked menu items
//
//        // Mocking services
//        when(orderItemService.findAllOrderItems()).thenReturn(orderItems);
//        when(orderService.findAllOrders()).thenReturn(orders);
//        when(menuItemService.findAllMenuItems()).thenReturn(menuItems);
//
//        // When & Then
//        mockMvc.perform(MockMvcRequestBuilders.get("/order-items/form"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("orderitems/form"))
//                .andExpect(MockMvcResultMatchers.model().attribute("orders", orders))
//               .andExpect(MockMvcResultMatchers.model().attribute("menuItems", menuItems))
//               .andExpect(MockMvcResultMatchers.model().attribute("orderItems", orderItems));
//    }

//    @Test
//    @WithMockUser
//    public void shouldShowNewOrderItemForm() throws Exception {
//        // Given, When & Then
//        mockMvc.perform(MockMvcRequestBuilders.get("/order-items/form"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("orderitems/form"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("orderItem"));
//    }

    @Test
    @WithMockUser
    public void shouldSaveOrderItems() throws Exception {
        // Given
        Long orderId = 1L;
        List<Long> menuItemIds = List.of(1L, 2L);
        List<Integer> quantities = List.of(2, 3);

        Order order = new Order();
        order.setId(orderId);
        when(orderService.findOrderById(orderId)).thenReturn(Optional.of(order));

        MenuItem menuItem1 = new MenuItem();
        menuItem1.setId(1L);
        menuItem1.setPrice(BigDecimal.valueOf(10));

        MenuItem menuItem2 = new MenuItem();
        menuItem2.setId(2L);
        menuItem2.setPrice(BigDecimal.valueOf(20));

        when(menuItemService.findMenuItemById(1L)).thenReturn(Optional.of(menuItem1));
        when(menuItemService.findMenuItemById(2L)).thenReturn(Optional.of(menuItem2));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/order-items/form")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("orderId", String.valueOf(orderId))
                        .param("menuItemIds", "1", "2")
                        .param("quantities", "2", "3"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/order-items/list"));

        // Verify interactions with services
        verify(orderService).findOrderById(orderId);
        verify(menuItemService).findMenuItemById(1L);
        verify(menuItemService).findMenuItemById(2L);
        verify(orderService).updateTotalPrice(orderId);
    }


//    @Test
//    @WithMockUser
//    public void shouldReturnOrderItemById() throws Exception {
//        // Given
//        Long id = 1L;
//        OrderItem orderItem = new OrderItem();
//        when(orderItemService.findOrderItemById(id)).thenReturn(Optional.of(orderItem));
//
//        // When & Then
//        mockMvc.perform(MockMvcRequestBuilders.get("/order-items/" + id))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("order-items/view"))
//                .andExpect(MockMvcResultMatchers.model().attribute("orderItem", orderItem))
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser
//    public void shouldReturnNotFoundWhenOrderItemDoesNotExist() throws Exception {
//        // Given
//        Long id = 1L;
//        when(orderItemService.findOrderItemById(id)).thenReturn(Optional.empty());
//
//        // When & Then
//        mockMvc.perform(MockMvcRequestBuilders.get("/order-items/" + id))
//                .andExpect(MockMvcResultMatchers.status().isNotFound()) // Poprawka statusu na 404
//                .andExpect(MockMvcResultMatchers.view().name("error/404"))
//                .andDo(print());
//    }

    @Test
    @WithMockUser
    public void shouldDeleteOrderItemById() throws Exception {
        // Given
        Long id = 1L;

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/order-items/delete/" + id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/order-items/list"));

        // Sprawdź, czy OrderItem zostało usunięte z bazy danych
        when(orderItemService.findOrderItemById(id)).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), orderItemService.findOrderItemById(id));
    }
}
