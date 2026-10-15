package ma.tourisme.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "transport")
@Getter
@Setter
public class TransportInfo extends Place {
    private String type;
    private Integer durationMin;
    private Double priceMad;
}
