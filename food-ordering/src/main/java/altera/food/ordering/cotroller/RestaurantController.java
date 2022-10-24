package altera.food.ordering.cotroller;

import altera.food.ordering.domain.dto.RestaurantDto;
import altera.food.ordering.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping(value = "")
    public ResponseEntity<Object> createNewrestaurant(@RequestBody RestaurantDto request) {
        return restaurantService.addrestaurant(request);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAllrestaurant() {
        return restaurantService.getrestaurant();
    }
    
}
