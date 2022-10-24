package altera.food.ordering.repository;

import altera.food.ordering.domain.dao.food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<food, Long> {
    
    List<food> findAllByCategoryId(Long categoryId);

    food findByfoodNameContaining(String foodName);

}
