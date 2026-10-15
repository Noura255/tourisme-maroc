package ma.tourisme.repository;

import ma.tourisme.model.TransportInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TransportInfoRepository extends MongoRepository<TransportInfo, String> {
    List<TransportInfo> findByCity(String city);
}
