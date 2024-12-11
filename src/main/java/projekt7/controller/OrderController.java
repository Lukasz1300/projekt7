package projekt7.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projekt7.entity.*;
import projekt7.service.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final OrderItemService orderItemService;

    @Autowired
    public OrderController(UserService userService, RestaurantService restaurantService, OrderService orderService, OrderItemService orderItemService) {
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.findAllOrders();
        model.addAttribute("orders", orders);
        return "orders/list";
    }

    @GetMapping("{id}")
    public String getOrderById(@PathVariable Long id, Model model, HttpServletResponse response) {
        Optional<Order> orderOptional = orderService.findOrderById(id);
        if (orderOptional.isPresent()) {
            model.addAttribute("order", orderOptional.get());
            return "orders/view";
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            model.addAttribute("error", "Menu item not found");
            return "error/404";
        }
    }

    @GetMapping("/user/{userId}")
    public String getOrdersByUserId(@PathVariable Long userId, Model model) {
        List<Order> orders = orderService.findOrdersByUserId(userId);
        model.addAttribute("orders", orders);
        return "orders/list";
    }

    @GetMapping("/restaurant/{restaurantId}")
    public String getOrdersByRestaurantId(@PathVariable Long restaurantId, Model model) {
        List<Order> orders = orderService.findOrdersByRestaurantId(restaurantId);
        model.addAttribute("orders", orders);
        return "orders/list";
    }

    @GetMapping("/form")
    public String showNewOrderForm(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("restaurants", restaurantService.findAllRestaurants());
        return "orders/form";
    }

    @PostMapping("/form")
    public String saveOrder(@ModelAttribute @Valid Order order, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findAllUsers());
            model.addAttribute("restaurants", restaurantService.findAllRestaurants());
            return "orders/form";
        }
        // Pobierz restaurację z bazy na podstawie ID
        Optional<Restaurant> restaurantOptional = restaurantService.findRestaurantById(order.getRestaurant().getId());
        if (restaurantOptional.isEmpty()) {
            model.addAttribute("error", "Restaurant not found.");
            return "orders/form";
        }
        // Pobierz użytkownika z bazy na podstawie ID
        Optional<User> userOptional = userService.findUserById(order.getUser().getId());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "User not found.");
            return "orders/form";
        }
        // Sprawdź zgodność miast
        Restaurant restaurant = restaurantOptional.get();
        User user = userOptional.get();

        if (restaurant.getAddressCity() == null || user.getAddressCity() == null ||
                !restaurant.getAddressCity().equalsIgnoreCase(user.getAddressCity())) {
            model.addAttribute("error", "Conflict: The entered city does not match the restaurant's city. Delivery not available. Cities must be the same.");
            model.addAttribute("users", userService.findAllUsers());
            model.addAttribute("restaurants", restaurantService.findAllRestaurants());
            return "orders/form";
        }

        orderService.saveOrder(order);
        return "redirect:/orders";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrderById(id);
        return "redirect:/orders";
    }
}

