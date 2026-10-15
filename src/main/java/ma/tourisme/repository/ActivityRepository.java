package ma.tourisme.repository;

import ma.tourisme.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends MongoRepository<Activity, String> {
    List<Activity> findByCity(String city);
}
