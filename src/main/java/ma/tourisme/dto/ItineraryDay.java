package ma.tourisme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.tourisme.model.Attraction;
import ma.tourisme.model.Restaurant;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItineraryDay {
    private int number;
    private List<Attraction> stops;
    private Restaurant lunch;
    private int totalMinutes;
}
