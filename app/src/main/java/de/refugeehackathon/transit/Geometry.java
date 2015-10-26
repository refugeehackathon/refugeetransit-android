package de.refugeehackathon.transit;

public class Geometry {
    public final double[] coordinates;

    public Geometry(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public double getLongitude() {
        return coordinates[0];
    }

    public double getLatitude() {
        return coordinates[1];
    }

}
