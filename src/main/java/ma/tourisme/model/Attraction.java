package ma.tourisme.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "attractions")
@Getter
@Setter
public class Attraction extends Place {
    private Double entryPriceMad;
    private Integer visitDurationMin;
    private String bestTimeToVisit;
    private String accessibility;
    private Boolean guidedTourAvailable;
    private String indoorOutdoor;
}
