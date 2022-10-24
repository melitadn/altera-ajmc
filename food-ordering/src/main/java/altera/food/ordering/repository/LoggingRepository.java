package altera.food.ordering.repository;

import altera.food.ordering.domain.dao.LoggingModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggingRepository extends MongoRepository<LoggingModel, String> {
}
