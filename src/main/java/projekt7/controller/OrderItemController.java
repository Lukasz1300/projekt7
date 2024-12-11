package projekt7.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import projekt7.entity.MenuItem;
import projekt7.entity.Order;
import projekt7.entity.OrderItem;
import projekt7.repository.MenuItemCategoryRepository;
import projekt7.repository.OrderRepository;
import projekt7.service.CategoryService;
import projekt7.service.MenuItemService;
import projekt7.service.OrderItemService;
import projekt7.service.OrderService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.IntStream;

@AllArgsConstructor
@Controller
@RequestMapping("/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;
    private final MenuItemService menuItemService;
    private final OrderService orderService;


    // widok formularza form.html
    @GetMapping("/form")
    public String showOrderItemForm(Model model) {
        List<Order> orders = orderService.findAllOrders();
        List<MenuItem> menuItems = menuItemService.findAllMenuItems();
        List<OrderItem> orderItems = orderItemService.findAllOrderItems();

        OrderItem orderItem = new OrderItem();
        model.addAttribute("orders", orders);
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("orderItems", orderItems); // Nowa pozycja zamówienia

        return "orderitems/form"; // Widok formularza
    }

    @PostMapping("/form")
    public String saveOrderItems(
            @RequestParam("orderId") Long orderId,
            @RequestParam("menuItemIds") List<Long> menuItemIds,
            @RequestParam("quantities") List<Integer> quantities,
            Model model) {

        // Sprawdzenie, czy ilości menuItems odpowiadają sobie
        if (menuItemIds.size() != quantities.size()) {
            throw new IllegalArgumentException("Mismatch between menu items and quantities.");
        }

        // Znalezienie zamówienia na podstawie podanego id
        Order order = orderService.findOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

        // Przetwarzanie każdego menu itemu
        for (int i = 0; i < menuItemIds.size(); i++) {
            Long menuItemId = menuItemIds.get(i);
            Integer quantity = quantities.get(i);

            // Znalezienie MenuItem na podstawie jego id
            MenuItem menuItem = menuItemService.findMenuItemById(menuItemId)
                    .orElseThrow(() -> new IllegalArgumentException("MenuItem not found with id: " + menuItemId));

            // Obliczanie całkowitej ceny zamówienia
            BigDecimal price = menuItem.getPrice().multiply(BigDecimal.valueOf(quantity));

            // Tworzenie i zapisanie OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(price);
            orderItemService.saveOrderItem(orderItem);
        }
        // Aktualizacja całkowitej ceny zamówienia
        orderService.updateTotalPrice(orderId);

        // Przekierowanie na stronę listy zamówień
        return "redirect:/order-items/list";
    }

    // metoda obsluguje widok list.html
    @GetMapping("/list")
    public String showOrderItemList(Model model) {
        List<OrderItem> orderItems = orderItemService.findAllOrderItems(); // Pobierz wszystkie pozycje zamówień
        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }
        model.addAttribute("orderItems", orderItems); // Przekaż listę do widoku
        return "orderitems/list"; // Widok list.html
    }

    @DeleteMapping("/delete/{id}")
    public String deleteOrderItem(@PathVariable Long id) {
        orderItemService.deleteOrderItemById(id);
        return "redirect:/order-items/list"; // Przekierowanie po usunięciu
    }
}

