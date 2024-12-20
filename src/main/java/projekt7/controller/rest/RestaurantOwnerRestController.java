package projekt7.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projekt7.entity.RestaurantOwner;
import projekt7.service.RestaurantOwnerService;

import java.util.Optional;

@RestController
@RequestMapping("/api/restaurant-owners")
public class RestaurantOwnerRestController {

    private final RestaurantOwnerService ownerService;

    @Autowired
    public RestaurantOwnerRestController(RestaurantOwnerService ownerService) {
        this.ownerService = ownerService;
    }

    // Pobranie właściciela według ID
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantOwner> getOwnerById(@PathVariable Long id) {
        Optional<RestaurantOwner> owner = ownerService.findOwnerById(id);
        return owner.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
