package ma.tourisme.repository;

import ma.tourisme.model.Attraction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AttractionRepository extends MongoRepository<Attraction, String> {
    List<Attraction> findByCity(String city);
    List<Attraction> findByCityAndCategory(String city, String category);
}
