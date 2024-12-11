package projekt7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projekt7.entity.DeliveryArea;
import projekt7.service.DeliveryAreaService;

import java.util.List;

@Controller
@RequestMapping("/deliveryareas")
public class DeliveryAreaController {

    private final DeliveryAreaService deliveryAreaService;

    @Autowired
    public DeliveryAreaController(DeliveryAreaService deliveryAreaService) {
        this.deliveryAreaService = deliveryAreaService;
    }

    // Pobranie wszystkich obszarów dostawy dla danej restauracji
    //localhost:8085/deliveryareas/restaurant/1
    @GetMapping("/restaurant/{restaurantId}")
    public String getDeliveryAreasByRestaurantId(@PathVariable Long restaurantId, Model model) {
        List<DeliveryArea> deliveryAreas = deliveryAreaService.findByRestaurantId(restaurantId);
        model.addAttribute("deliveryAreas", deliveryAreas);
        return "deliveryareas/list";
    }
}
