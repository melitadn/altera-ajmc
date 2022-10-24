package altera.food.ordering.service;


import altera.food.ordering.constant.AppConstant;
import altera.food.ordering.domain.dao.Category;
import altera.food.ordering.domain.dto.CategoryDto;
import altera.food.ordering.repository.CategoryRepository;
import altera.food.ordering.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<Object> addCategory(CategoryDto request) {
        log.info("Executing save new category");
        try {
            Category category = mapper.map(request, Category.class);
            categoryRepository.save(category);
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, mapper.map(category, CategoryDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Got an error when saving new category. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getCategoryById(CategoryDto dto){
        try {
            Optional<Category> category = categoryRepository.findById(dto.getId());
            return category.map(value -> ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, value, HttpStatus.OK)).orElseGet(() ->
                    ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND));
        }catch (Exception e){
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<Object> getCategory() {
        log.info("Executing save new category");
        try {
            List<Category> categoryList = categoryRepository.findAll();
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, categoryList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Got an error when saving new category. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
