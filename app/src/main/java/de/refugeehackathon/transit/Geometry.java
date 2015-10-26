package de.refugeehackathon.transit;

import android.support.annotation.FloatRange;

public class Geometry {
    public final double[] coordinates;

    public Geometry(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public
    @FloatRange(from = -180, to = 180)
    double getLongitude() {
        return coordinates[0];
    }

    public
    @FloatRange(from = -90, to = 90)
    double getLatitude() {
        return coordinates[1];
    }

}
