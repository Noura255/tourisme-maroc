package ma.tourisme.repository;

import ma.tourisme.model.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends MongoRepository<Hotel, String> {
    List<Hotel> findByCity(String city);
}
