package ma.tourisme.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "restaurants")
@Getter
@Setter
public class Restaurant extends Place {
    private String cuisine;
    private String type;
    private PriceRange avgPricePerPersonMad;
    private Integer priceLevel;
    private List<String> specialties;
    private Boolean halal;
    private Boolean vegetarianOptions;
    private Boolean alcoholServed;
    private Boolean reservationRequired;
}
