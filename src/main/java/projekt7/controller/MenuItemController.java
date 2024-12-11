package projekt7.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projekt7.entity.MenuItem;
import projekt7.service.MenuItemService;
import projekt7.service.RestaurantService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/menu-item")
public class MenuItemController {

    private final MenuItemService menuItemService;

    private final RestaurantService restaurantService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService, RestaurantService restaurantService) {
        this.menuItemService = menuItemService;
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public String getAllMenuItems(Model model) {
        List<MenuItem> menuItems = menuItemService.findAllMenuItems();
        model.addAttribute("menuItems", menuItems);
        return "menuitem/list";
    }

    @GetMapping("/restaurant/{restaurantId}")
    public String getMenuItemsByRestaurantId(@PathVariable Long restaurantId, Model model) {
        List<MenuItem> menuItems = menuItemService.findMenuItemsByRestaurantId(restaurantId);
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("restaurantId", restaurantId); // Dodajemy identyfikator restauracji, żeby wiedzieć, dla jakiej restauracji pokazujemy menu
        return "menuitem/list"; // Przykładowy widok
    }


    // Pobranie pozycji menu według ID
    @GetMapping("/{id}")
    public String getMenuItemById(@PathVariable("id") Long id, Model model, HttpServletResponse response) {
        Optional<MenuItem> menuItemOptional = menuItemService.findMenuItemById(id);
        if (menuItemOptional.isPresent()) {
            model.addAttribute("menuItem", menuItemOptional.get());
            return "menuitem/detail";  // Widok szczegółów pozycji menu
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            model.addAttribute("error", "Menu item not found");
            return "error/404";
        }
    }

    @GetMapping("/form")
    public String showMenuItemForm(Model model) {
        model.addAttribute("menuItem", new MenuItem());
        model.addAttribute("restaurants", restaurantService.findAllRestaurants());
        return "menuitem/form";
    }

    @PostMapping
    public String saveMenuItem(@Valid @ModelAttribute MenuItem menuItem, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurants", restaurantService.findAllRestaurants()); // Upewnij się, że listujesz dostępne restauracje w formularzu
            return "menuitem/form";  // Jeśli występują błędy walidacji, wyświetl ponownie formularz
        }
        menuItemService.saveMenuItem(menuItem);
        return "redirect:/menu-item";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItemById(id); // Usunięcie pozycji menu
        return "redirect:/menu-item"; // Przekierowanie na stronę z listą pozycji menu
    }

}
