package ma.tourisme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.tourisme.model.Hotel;

import java.util.List;

@Getter
@AllArgsConstructor
public class RecommendationResult {
    private String city;
    private Hotel hotel;
    private List<ItineraryDay> days;
}
