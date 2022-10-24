package altera.food.ordering.repository;

import altera.food.ordering.domain.dao.User;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface UserRepository extends JpaRepository<User, Long> {
    User getDistinctTopByUsername(String username);
}
