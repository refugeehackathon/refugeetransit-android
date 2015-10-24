package de.refugeehackathon.transit;

public class POI {
    public final POIType type;
    public final Geometry geometry;
    public final Properties properties;

    public POI(POIType type, Geometry geometry, Properties properties) {
        this.type = type;
        this.geometry = geometry;
        this.properties = properties;
    }
}
