package altera.food.ordering.service;


import altera.food.ordering.constant.AppConstant;
import altera.food.ordering.domain.dao.restaurant;
import altera.food.ordering.domain.dto.RestaurantDto;
import altera.food.ordering.repository.RestaurantRepository;
import altera.food.ordering.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RestaurantService {
    
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<Object> addrestaurant(RestaurantDto request) {
        log.info("Executing save new restaurant");
        try {
            restaurant restaurant = mapper.map(request, restaurant.class);
            restaurantRepository.save(restaurant);
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, mapper.map(restaurant, RestaurantDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Got an error when saving new restaurant. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getrestaurant() {
        log.info("Executing save new restaurant");
        try {
            List<restaurant> restaurants = restaurantRepository.findAll();
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, restaurants, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Got an error when saving new restaurant. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
