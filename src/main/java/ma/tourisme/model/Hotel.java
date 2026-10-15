package ma.tourisme.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "hotels")
@Getter
@Setter
public class Hotel extends Place {
    private String type;
    private Integer stars;
    private PriceRange pricePerNightMad;
    private Integer priceLevel;
    private List<String> amenities;
    private List<String> roomsTypes;
    private Boolean breakfastIncluded;
    private Double distanceToCenterKm;
    private String bookingUrl;
}
