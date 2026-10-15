package ma.tourisme.service;

import lombok.RequiredArgsConstructor;
import ma.tourisme.dto.ItineraryDay;
import ma.tourisme.dto.RecommendationRequest;
import ma.tourisme.dto.RecommendationResult;
import ma.tourisme.exception.ResourceNotFoundException;
import ma.tourisme.model.Attraction;
import ma.tourisme.model.Hotel;
import ma.tourisme.model.Restaurant;
import ma.tourisme.repository.AttractionRepository;
import ma.tourisme.repository.HotelRepository;
import ma.tourisme.repository.RestaurantRepository;
import ma.tourisme.util.GeoUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Partie « intelligente » : score des attractions, puis itinéraire jour par jour
 * (on part de la mieux notée, puis on enchaîne avec la plus proche tant qu'il reste du temps).
 */
@Service
@RequiredArgsConstructor
public class RecommendationService {
    static final int MINUTES_PER_DAY = 360;
    static final int DEFAULT_VISIT_MIN = 60;

    private final AttractionRepository attractionRepo;
    private final HotelRepository hotelRepo;
    private final RestaurantRepository restaurantRepo;

    /** Score sur 100 : centres d'intérêt (60) + profil de voyageur (20) + note (20). Donnée absente = valeur neutre. */
    public double score(Attraction a, RecommendationRequest req) {
        List<String> wanted = req.getInterests() == null ? List.of() : req.getInterests();
        double interests;
        if (wanted.isEmpty()) {
            interests = 30;
        } else {
            Set<String> terms = new HashSet<>();
            if (a.getTags() != null) a.getTags().forEach(t -> terms.add(norm(t)));
            terms.add(norm(a.getCategory()));
            terms.add(norm(a.getSubcategory()));
            long hits = wanted.stream().filter(w -> terms.contains(norm(w))).count();
            interests = 60.0 * hits / wanted.size();
        }
        String type = norm(req.getTravelerType());
        double suitable;
        if (type.isEmpty() || a.getSuitableFor() == null) {
            suitable = 10;
        } else {
            suitable = a.getSuitableFor().stream().map(RecommendationService::norm).anyMatch(type::equals) ? 20 : 0;
        }
        double rating = a.getRating() == null ? 10 : a.getRating() / 5.0 * 20;
        return interests + suitable + rating;
    }

    public RecommendationResult recommend(RecommendationRequest req) {
        String city = req.getCity();
        int budget = req.getBudgetLevel() == null ? 4 : req.getBudgetLevel();

        List<Attraction> pool = attractionRepo.findByCity(city);
        if (pool.isEmpty()) throw new ResourceNotFoundException("Aucune attraction pour la ville : " + city);

        Map<String, Double> scores = new HashMap<>();
        pool.forEach(a -> scores.put(a.getId(), score(a, req)));
        List<Attraction> remaining = new ArrayList<>(pool);
        remaining.sort(Comparator.comparingDouble((Attraction a) -> scores.get(a.getId())).reversed());

        List<Restaurant> restaurants = restaurantRepo.findByCity(city);
        List<Restaurant> affordable = restaurants.stream()
                .filter(r -> r.getPriceLevel() == null || r.getPriceLevel() <= budget).toList();
        if (affordable.isEmpty()) affordable = restaurants;

        List<ItineraryDay> days = new ArrayList<>();
        for (int d = 1; d <= req.getDays() && !remaining.isEmpty(); d++) {
            List<Attraction> stops = new ArrayList<>();
            int used = 0;
            Attraction current = remaining.get(0);
            while (current != null) {
                remaining.remove(current);
                stops.add(current);
                used += duration(current);
                final Attraction last = current;
                final int usedNow = used;
                current = remaining.stream()
                        .filter(a -> usedNow + duration(a) <= MINUTES_PER_DAY)
                        .min(Comparator.comparingDouble(a -> GeoUtils.distanceKm(last, a)))
                        .orElse(null);
            }
            Attraction middle = stops.get(stops.size() / 2);
            Restaurant lunch = affordable.stream()
                    .min(Comparator.comparingDouble(r -> GeoUtils.distanceKm(middle, r)))
                    .orElse(null);
            days.add(new ItineraryDay(d, stops, lunch, used));
        }
        return new RecommendationResult(city, pickHotel(city, budget), days);
    }

    private Hotel pickHotel(String city, int budget) {
        List<Hotel> hotels = hotelRepo.findByCity(city);
        return hotels.stream()
                .filter(h -> h.getPriceLevel() == null || h.getPriceLevel() <= budget)
                .max(Comparator.comparingDouble((Hotel h) -> h.getRating() == null ? 0 : h.getRating())
                        .thenComparingInt(h -> h.getStars() == null ? 0 : h.getStars()))
                .orElse(hotels.isEmpty() ? null : hotels.get(0));
    }

    private static int duration(Attraction a) {
        return a.getVisitDurationMin() == null ? DEFAULT_VISIT_MIN : a.getVisitDurationMin();
    }

    private static String norm(String s) {
        return s == null ? "" : s.toLowerCase(Locale.ROOT).trim();
    }
}
