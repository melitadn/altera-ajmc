package altera.food.ordering.repository;

import altera.food.ordering.domain.dao.restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<restaurant, Long> {
    
}
