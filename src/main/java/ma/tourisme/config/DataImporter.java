package ma.tourisme.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Au démarrage, charge src/main/resources/data/*.json dans MongoDB
 * pour chaque collection encore vide. Les documents sont insérés tels quels.
 */
@Component
@RequiredArgsConstructor
public class DataImporter implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataImporter.class);
    private static final List<String> COLLECTIONS =
            List.of("cities", "attractions", "hotels", "restaurants", "activities", "events", "transport");

    private final MongoTemplate mongo;
    private final ObjectMapper mapper;

    @Override
    public void run(String... args) throws Exception {
        for (String name : COLLECTIONS) {
            if (mongo.getCollection(name).countDocuments() > 0) {
                log.info("Collection {} déjà remplie : import ignoré", name);
                continue;
            }
            try (InputStream in = new ClassPathResource("data/" + name + ".json").getInputStream()) {
                List<Map<String, Object>> rows = mapper.readValue(in, new TypeReference<List<Map<String, Object>>>() {});
                if (rows.isEmpty()) continue;
                List<Document> docs = rows.stream().map(Document::new).toList();
                mongo.getCollection(name).insertMany(docs);
                log.info("Collection {} : {} documents importés", name, docs.size());
            }
        }
    }
}
