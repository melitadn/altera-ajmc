package altera.food.ordering.cotroller;

import altera.food.ordering.domain.dto.CategoryDto;
import altera.food.ordering.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(value = "")
    public ResponseEntity<Object> createNewCategory(@RequestBody CategoryDto request) {
        return categoryService.addCategory(request);
    }
    @GetMapping(value = "")
    public ResponseEntity<Object> getAllCategory() {
        return categoryService.getCategory();
    }

    @PostMapping(value = "byid")
    public ResponseEntity<Object> getCategoryById(@RequestBody CategoryDto dto){
        return categoryService.getCategoryById(dto);
    }
}
