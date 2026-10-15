package ma.tourisme.controller;

import lombok.RequiredArgsConstructor;
import ma.tourisme.model.City;
import ma.tourisme.model.Place;
import ma.tourisme.service.PlaceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Petite API JSON (optionnelle) : /api/cities et /api/places/{type}?city=Fès */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {
    private final PlaceService places;

    @GetMapping("/cities")
    public List<City> cities() { return places.cities(); }

    @GetMapping("/places/{type}")
    public List<? extends Place> list(@PathVariable String type, @RequestParam String city) {
        return places.listByType(type, city);
    }
}
