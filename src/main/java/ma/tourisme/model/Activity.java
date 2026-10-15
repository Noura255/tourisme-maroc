package ma.tourisme.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "activities")
@Getter
@Setter
public class Activity extends Place {
    private String activityType;
    private Double priceMad;
    private Double durationHours;
    private String difficulty;
    private Integer minAge;
    private String season;
    private String providerName;
    private Integer groupSize;
}
