package altera.food.ordering.service;


import altera.food.ordering.constant.AppConstant;
import altera.food.ordering.domain.dao.restaurant;
import altera.food.ordering.domain.dao.Category;
import altera.food.ordering.domain.dao.food;
import altera.food.ordering.domain.dto.FoodDto;
import altera.food.ordering.domain.dto.FoodListDto;
import altera.food.ordering.repository.RestaurantRepository;
import altera.food.ordering.repository.CategoryRepository;
import altera.food.ordering.repository.FoodRepository;
import altera.food.ordering.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FoodService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<Object> addfood(FoodDto request) {
        log.info("Executing add new food");
        try {
            Optional<Category> category = categoryRepository.findById(request.getCategory().getId());
            if (category.isEmpty()) {
                log.info("Category [{}] not found", request.getCategory().getId());
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }

            Optional<restaurant> restaurant = restaurantRepository.findById(request.getRestaurant().getId());
            if (restaurant.isEmpty()) {
                log.info("restaurant [{}] not found", request.getRestaurant().getId());
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }

            food food = mapper.map(request, food.class);
            food.setRestaurant(restaurant.get());
            food.setCategory(category.get());
            foodRepository.save(food);
            
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, mapper.map(food, FoodDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Got an error when saving new food. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getAllfood(Long categoryId) {
        try {
            log.info("Executing get all food by category [{}]", categoryId);
            List<food> foods;
            List<FoodDto> foodDtoList = new ArrayList<>();

            if (categoryId != null) foods = foodRepository.findAllByCategoryId(categoryId);
            else foods = foodRepository.findAll();

            for (food food : foods) {
                foodDtoList.add(mapper.map(food, FoodDto.class));
            }

            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, foodDtoList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Got an error when get all food by category. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getAllfoodPagination(FoodListDto request) {
        try {
            log.info("Executing get all food with pagination");
            int page = null == request.getPage() ? 0 : request.getPage();
            int size = null == request.getSize() ? 1 : request.getSize();
            Pageable pageable = PageRequest.of(page, size);
            Page<food> foodPage = foodRepository.findAll(pageable);

            log.info("Mapping page into dtos. Size: [{}]", foodPage.getTotalElements());
            List<FoodDto> foodDtoList = new ArrayList<>();
            
            for (food food : foodPage.getContent()) {
                foodDtoList.add(mapper.map(food, FoodDto.class));
            }

            FoodListDto foodListDto = FoodListDto.builder()
                .foods(foodDtoList)
                .size(foodPage.getSize())
                .page(foodPage.getNumber())
                .totalPage(foodPage.getTotalPages())
                .build();

                return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, foodListDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Got an error when get all food with pagination. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getAllfoodSortByCategory(Sort.Direction direction) {
        try {
            log.info("Executing get all food sort by category [{}]", direction);
            List<food> foods = foodRepository.findAll(Sort.by(direction, "category.id"));
            List<FoodDto> foodDtoList = new ArrayList<>();
        
            for (food food : foods) {
                foodDtoList.add(mapper.map(food, FoodDto.class));
            }

            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, foodDtoList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Got an error when get all food sort by category. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> searchfoodByName(String foodName) {
        try {
            log.info("Executing search food by name: [{}]", foodName);
            food food = foodRepository.findByfoodNameContaining(foodName);

            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, mapper.map(food, FoodDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Got an error when search food by food name. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
