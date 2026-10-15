package ma.tourisme.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;

import java.util.List;

/** Champs communs à toutes les collections (voir le plan de collecte, section 2). */
@Getter
@Setter
public abstract class Place {
    @Id
    private String id;
    private String city;
    private String region;
    private String name;
    private String category;
    private String subcategory;
    private String description;
    private String address;
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;
    private String phone;
    private String website;
    private String openingHours;
    private Double rating;
    private Integer reviewsCount;
    private List<String> images;
    private List<String> tags;
    private List<String> suitableFor;
    private String source;
    private String lastVerified;
}
