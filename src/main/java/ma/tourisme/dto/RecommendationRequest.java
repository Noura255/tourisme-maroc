package ma.tourisme.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RecommendationRequest {
    @NotBlank(message = "Choisis une ville")
    private String city;
    private String travelerType = "couple";
    @NotNull @Min(1) @Max(4)
    private Integer budgetLevel = 2;
    private List<String> interests = new ArrayList<>();
    @NotNull @Min(value = 1, message = "Minimum 1 jour") @Max(value = 7, message = "Maximum 7 jours")
    private Integer days = 2;
}
