# Guide touristique du Maroc : Spring Boot + MongoDB

Données de départ : Marrakech, Fès, Meknès (Personne 1). Chaque membre de l'équipe ajoute ses villes dans `src/main/resources/data/*.json`.

## Lancer le projet
1. Installer JDK 17+ et MongoDB (ou créer un cluster gratuit sur MongoDB Atlas).
2. (Atlas uniquement) définir la variable d'environnement :
   `MONGODB_URI="mongodb+srv://utilisateur:motdepasse@cluster.mongodb.net/tourisme_maroc"`
3. `mvn spring-boot:run` (ou « Run » sur `TourismeApplication` dans l'IDE).
4. Ouvrir http://localhost:8080

Au premier démarrage, `DataImporter` remplit les collections vides à partir des JSON.
Pour réimporter après modification des JSON : vider la collection concernée (`db.attractions.drop()`) puis relancer.

## Pages
- `/` villes · `/cities/{nom}` fiche ville · `/places/{type}/{id}` détail · `/recommend` itinéraire
- API JSON : `/api/cities`, `/api/places/attractions?city=Fès`

## Structure
`model` documents MongoDB · `repository` Spring Data · `service` logique (PlaceService, RecommendationService) · `controller` pages et API · `dto` formulaire et résultat · `config/DataImporter` import des JSON
