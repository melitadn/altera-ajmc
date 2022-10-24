package altera.food.ordering.service;

import altera.food.ordering.domain.dao.LoggingModel;
import altera.food.ordering.repository.LoggingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class LoggingSvcImpl implements LoggingSvc{

    private final LoggingRepository loggingRepository;

    @Autowired
    public LoggingSvcImpl(LoggingRepository loggingRepository) {
        this.loggingRepository = loggingRepository;
    }

    @Override
    public void createLog(HashMap<String, Object> map, String type) {
        LoggingModel logging = new LoggingModel();
        logging.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        logging.setMap(map);
        logging.setType(type);
        loggingRepository.save(logging);
    }
}
