package de.refugeehackathon.transit;

public class POI {
    public final POIType type;
    public final double latitude;
    public final double longitude;
    public final String title;
    public final String description;

    public POI(POIType type, double latitude, double longitude, String title, String description) {
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
    }
}
