package de.refugeehackathon.transit;

public class POI {
    public final Geometry geometry;
    public final Properties properties;

    public POI(Geometry geometry, Properties properties) {
        this.geometry = geometry;
        this.properties = properties;
    }
}
