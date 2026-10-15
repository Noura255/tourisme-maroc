package ma.tourisme.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "events")
@Getter
@Setter
public class Event extends Place {
    private String month;
    private String dateStart;
    private String dateEnd;
    private Double priceMad;
}
