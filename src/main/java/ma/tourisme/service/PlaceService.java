package ma.tourisme.service;

import lombok.RequiredArgsConstructor;
import ma.tourisme.exception.ResourceNotFoundException;
import ma.tourisme.model.*;
import ma.tourisme.repository.*;
import ma.tourisme.util.GeoUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private static final Map<String, Class<? extends Place>> TYPES = Map.of(
            "attractions", Attraction.class,
            "hotels", Hotel.class,
            "restaurants", Restaurant.class,
            "activities", Activity.class,
            "events", Event.class);

    private final CityRepository cityRepo;
    private final AttractionRepository attractionRepo;
    private final HotelRepository hotelRepo;
    private final RestaurantRepository restaurantRepo;
    private final ActivityRepository activityRepo;
    private final EventRepository eventRepo;
    private final TransportInfoRepository transportRepo;
    private final MongoTemplate mongo;

    public List<City> cities() {
        return cityRepo.findAll(Sort.by("name"));
    }

    public City city(String name) {
        return cityRepo.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Ville inconnue : " + name));
    }

    public List<Attraction> attractions(String city, String category) {
        return (category == null || category.isBlank())
                ? attractionRepo.findByCity(city)
                : attractionRepo.findByCityAndCategory(city, category);
    }

    public List<String> attractionCategories(String city) {
        return attractionRepo.findByCity(city).stream()
                .map(Attraction::getCategory).filter(Objects::nonNull).distinct().sorted().toList();
    }

    public List<Hotel> hotels(String city) { return hotelRepo.findByCity(city); }
    public List<Restaurant> restaurants(String city) { return restaurantRepo.findByCity(city); }
    public List<Activity> activities(String city) { return activityRepo.findByCity(city); }
    public List<Event> events(String city) { return eventRepo.findByCity(city); }
    public List<TransportInfo> transports(String city) { return transportRepo.findByCity(city); }

    /** Liste générique pour l'API REST : type = attractions | hotels | restaurants | activities | events. */
    public List<? extends Place> listByType(String type, String city) {
        Class<? extends Place> cls = TYPES.get(type);
        if (cls == null) throw new ResourceNotFoundException("Type inconnu : " + type);
        return mongo.find(Query.query(Criteria.where("city").is(city)), cls);
    }

    public Place findPlace(String type, String id) {
        Class<? extends Place> cls = TYPES.get(type);
        if (cls == null) throw new ResourceNotFoundException("Type inconnu : " + type);
        Place place = mongo.findById(id, cls);
        if (place == null) throw new ResourceNotFoundException("Lieu introuvable : " + id);
        return place;
    }

    /** Attractions les plus proches (même ville), triées par distance. */
    public List<Attraction> nearby(Place p, int limit) {
        if (p.getLocation() == null) return List.of();
        return attractionRepo.findByCity(p.getCity()).stream()
                .filter(a -> a.getLocation() != null && !a.getId().equals(p.getId()))
                .sorted(Comparator.comparingDouble(a -> GeoUtils.distanceKm(p, a)))
                .limit(limit)
                .toList();
    }

    /** Champs affichés sur la fiche détail (libellé -> valeur, null = à compléter). */
    public Map<String, Object> details(Place p) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("Adresse", p.getAddress());
        m.put("Coordonnées (lat, lon)", p.getLocation() == null ? null
                : p.getLocation().getY() + ", " + p.getLocation().getX());
        m.put("Téléphone", p.getPhone());
        m.put("Site web", p.getWebsite());
        m.put("Horaires", p.getOpeningHours());
        m.put("Note", p.getRating());
        if (p instanceof Attraction a) {
            m.put("Prix d'entrée (MAD)", a.getEntryPriceMad());
            m.put("Durée de visite (min)", a.getVisitDurationMin());
            m.put("Meilleur moment", a.getBestTimeToVisit());
            m.put("Intérieur / extérieur", a.getIndoorOutdoor());
        } else if (p instanceof Hotel h) {
            m.put("Type", h.getType());
            m.put("Étoiles", h.getStars());
            m.put("Prix par nuit (MAD)", range(h.getPricePerNightMad()));
            m.put("Niveau de prix (1-4)", h.getPriceLevel());
            m.put("Équipements", h.getAmenities());
        } else if (p instanceof Restaurant r) {
            m.put("Cuisine", r.getCuisine());
            m.put("Type", r.getType());
            m.put("Prix par personne (MAD)", range(r.getAvgPricePerPersonMad()));
            m.put("Niveau de prix (1-4)", r.getPriceLevel());
            m.put("Spécialités", r.getSpecialties());
        } else if (p instanceof Activity a) {
            m.put("Type d'activité", a.getActivityType());
            m.put("Prix (MAD)", a.getPriceMad());
            m.put("Durée (heures)", a.getDurationHours());
            m.put("Saison", a.getSeason());
        } else if (p instanceof Event e) {
            m.put("Mois", e.getMonth());
            m.put("Début", e.getDateStart());
            m.put("Fin", e.getDateEnd());
        }
        m.put("Source", p.getSource());
        m.put("Dernière vérification", p.getLastVerified());
        return m;
    }

    private static String range(PriceRange r) {
        return (r == null || r.getMin() == null || r.getMax() == null) ? null : r.getMin() + " – " + r.getMax();
    }
}
