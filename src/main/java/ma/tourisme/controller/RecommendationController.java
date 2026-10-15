package ma.tourisme.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.tourisme.dto.RecommendationRequest;
import ma.tourisme.model.City;
import ma.tourisme.service.PlaceService;
import ma.tourisme.service.RecommendationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecommendationController {
    private final PlaceService places;
    private final RecommendationService recommendations;

    @ModelAttribute("cities")
    public List<City> cities() { return places.cities(); }

    @ModelAttribute("travelerTypes")
    public List<String> travelerTypes() { return List.of("couple", "famille", "solo", "groupe"); }

    @ModelAttribute("interestOptions")
    public List<String> interestOptions() {
        return List.of("photo", "histoire", "architecture", "culture", "jardin", "gastronomie",
                "shopping", "artisanat", "famille", "romantique", "calme", "médina", "UNESCO", "gratuit");
    }

    @GetMapping("/recommend")
    public String form(Model model) {
        model.addAttribute("form", new RecommendationRequest());
        return "recommend-form";
    }

    @PostMapping("/recommend")
    public String submit(@Valid @ModelAttribute("form") RecommendationRequest form,
                         BindingResult errors, Model model) {
        if (errors.hasErrors()) return "recommend-form";
        model.addAttribute("result", recommendations.recommend(form));
        return "itinerary";
    }
}
