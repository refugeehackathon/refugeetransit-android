package de.refugeehackathon.transit;

public class Properties {

    public final String name;
    public final String description;
    public final POIType poitype;

    public Properties(String name, String description, POIType poitype) {
        this.name = name;
        this.description = description;
        this.poitype = poitype;
    }

}
