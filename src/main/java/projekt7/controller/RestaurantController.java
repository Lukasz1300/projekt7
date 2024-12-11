package projekt7.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projekt7.entity.Restaurant;
import projekt7.entity.RestaurantOwner;
import projekt7.service.RestaurantOwnerService;
import projekt7.service.RestaurantService;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantOwnerService restaurantOwnerService;
    private final RestaurantService restaurantService;


    @Autowired
    public RestaurantController(RestaurantOwnerService restaurantOwnerService, RestaurantService restaurantService) {
        this.restaurantOwnerService = restaurantOwnerService;
        this.restaurantService = restaurantService;
    }

    // Pobranie wszystkich restauracji
    @GetMapping
    public String getAllRestaurants(Model model) {
        List<Restaurant> restaurants = restaurantService.findAllRestaurants();
        model.addAttribute("restaurants", restaurants);
        return "restaurants/list"; // widok "list1.html" w katalogu "restaurants"
    }

    // Pobranie restauracji według ID
    @GetMapping("/{id}")
    public String getRestaurantById(@PathVariable Long id, Model model, HttpServletResponse response) {
        Optional<Restaurant> restaurant = restaurantService.findRestaurantById(id);
        if (restaurant.isPresent()) {
            model.addAttribute("restaurant", restaurant.get());
            return "restaurants/view"; // Widok, gdy restauracja istnieje
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Ustawienie statusu 404
            model.addAttribute("message", "Restaurant not found");
            return "error/404"; // Zwrócenie widoku błędu 404
        }
    }

    // Pobranie restauracji według ID właściciela
    @GetMapping("/owner/{ounerId}")
    public String getRestaurantsByOwnerId(@PathVariable Long ounerId, Model model) {
        log.info("Pobieranie restauracji dla właściciela o ID: {}", ounerId);
        List<Restaurant> restaurants = restaurantService.findRestaurantsByOwnerId(ounerId);
        log.info("Znaleziono restauracje: {}", restaurants);
        model.addAttribute("restaurants", restaurants);
        return "restaurants/list";
    }

    @GetMapping("/form/{id}")
    public String showNewRestaurantForm(@PathVariable("id") Long id, Model model) {
        // Pobieranie właściciela na podstawie id
        Optional<RestaurantOwner> owner = restaurantOwnerService.findOwnerById(id);
        if (owner.isEmpty()) {
            log.error("Owner with ID {} not found", id); // Logowanie, jeśli właściciel nie zostanie znaleziony
        } else {
            model.addAttribute("owner", owner.get());
        }

        // Pobieranie wszystkich właścicieli
        List<RestaurantOwner> owners = restaurantOwnerService.findAllOwners();  // Dodaj metodę w service do pobrania wszystkich właścicieli
        model.addAttribute("owners", owners);  // Przekazanie listy właścicieli do widoku

        model.addAttribute("restaurant", new Restaurant()); // Tworzymy nową instancję restauracji
        return "restaurants/form"; // Zwraca widok "restaurant/form"
    }

    // Zapisywanie restauracji
    @PostMapping
    public String saveRestaurant(@Valid @ModelAttribute Restaurant restaurant, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // Jeśli są błędy walidacji, wróć do formularza
            return "restaurants/form";
        }
        restaurantService.saveRestaurant(restaurant);
        return "redirect:/restaurants";
    }

    // Usuwanie restauracji według ID
    @DeleteMapping("/delete/{id}")
    public String deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurantById(id);
        return "redirect:/restaurants";
    }
}

