package ma.tourisme.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "cities")
@Getter
@Setter
public class City extends Place {
    private Integer population;
    private String bestSeason;
    private String climate;
    private Boolean airportNearby;
    private Boolean trainStation;
    private List<String> languages;
    private Double averageDailyBudgetMad;
    private List<String> safetyTips;
    private List<String> localSpecialties;
    private Integer typicalDurationDays;
    private List<String> nearbyCities;
}
