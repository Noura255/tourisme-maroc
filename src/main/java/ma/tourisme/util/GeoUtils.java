package ma.tourisme.util;

import ma.tourisme.model.Place;

public final class GeoUtils {
    private static final double EARTH_RADIUS_KM = 6371.0;

    private GeoUtils() {}

    /** Distance à vol d'oiseau (haversine). Renvoie Double.MAX_VALUE si une position manque. */
    public static double distanceKm(Place a, Place b) {
        if (a.getLocation() == null || b.getLocation() == null) return Double.MAX_VALUE;
        double lat1 = Math.toRadians(a.getLocation().getY());
        double lat2 = Math.toRadians(b.getLocation().getY());
        double dLat = lat2 - lat1;
        double dLon = Math.toRadians(b.getLocation().getX() - a.getLocation().getX());
        double h = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * EARTH_RADIUS_KM * Math.asin(Math.sqrt(h));
    }
}
