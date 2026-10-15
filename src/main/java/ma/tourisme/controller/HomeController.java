package ma.tourisme.controller;

import lombok.RequiredArgsConstructor;
import ma.tourisme.model.City;
import ma.tourisme.model.Place;
import ma.tourisme.service.PlaceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final PlaceService places;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("cities", places.cities());
        return "index";
    }

    @GetMapping("/cities/{name}")
    public String city(@PathVariable String name,
                       @RequestParam(required = false) String category,
                       Model model) {
        City city = places.city(name);
        model.addAttribute("city", city);
        model.addAttribute("category", category);
        model.addAttribute("categories", places.attractionCategories(name));
        model.addAttribute("attractions", places.attractions(name, category));
        model.addAttribute("hotels", places.hotels(name));
        model.addAttribute("restaurants", places.restaurants(name));
        model.addAttribute("activities", places.activities(name));
        model.addAttribute("events", places.events(name));
        model.addAttribute("transports", places.transports(name));
        return "city";
    }

    @GetMapping("/places/{type}/{id}")
    public String detail(@PathVariable String type, @PathVariable String id, Model model) {
        Place place = places.findPlace(type, id);
        model.addAttribute("place", place);
        model.addAttribute("details", places.details(place));
        model.addAttribute("nearby", places.nearby(place, 4));
        return "place-detail";
    }
}
