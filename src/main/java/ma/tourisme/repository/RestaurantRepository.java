package ma.tourisme.repository;

import ma.tourisme.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
    List<Restaurant> findByCity(String city);
}
