package altera.food.ordering.cotroller;

import altera.food.ordering.domain.dto.FoodDto;
import altera.food.ordering.domain.dto.FoodListDto;
import altera.food.ordering.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @PostMapping(value = "")
    public ResponseEntity<Object> createNewfood(@RequestBody FoodDto request) {
        return foodService.addfood(request);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAllfood(@RequestParam(value = "category_id", required = false) Long categoryId) {
        return foodService.getAllfood(categoryId);
    }

    @PostMapping(value = "/pagination")
    public ResponseEntity<Object> getAllfoodPagination(@RequestBody FoodListDto request) {
        return foodService.getAllfoodPagination(request);
    }

    @GetMapping(value = "/sort-by-category")
    public ResponseEntity<Object> getAllfoodSortByCategory(@RequestParam(value = "sort", required = true) Sort.Direction direction) {
        return foodService.getAllfoodSortByCategory(direction);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Object> searchfood(@RequestParam(value = "food_name") String foodName) {
        return foodService.searchfoodByName(foodName);
    }
    
    
}
