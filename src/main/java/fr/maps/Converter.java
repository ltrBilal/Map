package fr.maps;

import javafx.geometry.Point2D;

public class Converter {
    private static final double EARTH_RADIUS = 6371.0; // Rayon de la Terre en km
    private static final double MAX_DISTORTION = 0.1; // Limite de distorsion maximale (10 %)
    private final double lon0; // Longitude du centre de la projection
    private final double lat0; // Latitude du centre de la projection
    private final double scale; // Echelle de la projection

    public Converter(double lon0, double lat0, double scale) {
        this.lon0 = lon0;
        this.lat0 = lat0;
        this.scale = scale;
    }

    public Point2D project(double lon, double lat) {
        double phi1 = Math.toRadians(lat0);
        double phi2 = Math.toRadians(lat);
        double lambda1 = Math.toRadians(lon0);
        double lambda2 = Math.toRadians(lon);

        double x, y;
        if (lon == lon0 && lat == lat0) {
            x = 0;
            y = 0;
        } else {
            // Projection Mercator
            double k = Math.cos(phi1) * Math.sqrt(1 - Math.pow(Math.sin(phi2), 2));
            double phi = Math.asin(Math.sin(phi2) / Math.cosh(k * (lambda2 - lambda1)));
            double lambda = lambda1 + Math.atan2(Math.sinh(k * (lambda2 - lambda1)), Math.cos(phi2)) / k;
            x = EARTH_RADIUS * Math.toDegrees(lambda - lambda1) * Math.cos(phi1) * scale;
            y = EARTH_RADIUS * Math.toDegrees(phi - phi1) * scale;
        }

        // Correction de la distorsion
        Point2D center = projectInternal(lon0, lat0);
        double maxDistortion = center.distance(projectInternal(lon0 + 1, lat0)) * MAX_DISTORTION;
        double distortion = Math.sqrt(Math.pow(x - center.getX(), 2) + Math.pow(y - center.getY(), 2)) / maxDistortion;
        if (distortion > 1) {
            x = center.getX() + (x - center.getX()) / distortion;
            y = center.getY() + (y - center.getY()) / distortion;
        }

        return new Point2D(x, y);
    }

    private Point2D projectInternal(double lon, double lat) {
        double phi1 = Math.toRadians(lat0);
        double phi2 = Math.toRadians(lat);
        double lambda1 = Math.toRadians(lon0);
        double lambda2 = Math.toRadians(lon);

        // Projection Mercator
        double k = Math.cos(phi1) * Math.sqrt(1 - Math.pow(Math.sin(phi2), 2));
        double phi = Math.asin(Math.sin(phi2) / Math.cosh(k * (lambda2 - lambda1)));
        double lambda = lambda1 + Math.atan2(Math.sinh(k * (lambda2 - lambda1)), Math.cos(phi2)) / k;
        double x = EARTH_RADIUS * Math.toDegrees(lambda - lambda1) * Math.cos(phi1) * scale;
        double y = EARTH_RADIUS * Math.toDegrees(phi - phi1) * scale;

        return new Point2D(x, y);
    }
}







