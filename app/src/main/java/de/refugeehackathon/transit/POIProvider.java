package de.refugeehackathon.transit;

import java.util.ArrayList;
import java.util.List;

public class POIProvider {

    public List<POI> getPOIS() {
        final List<POI> res = new ArrayList<>();
        res.add(new POI(new Geometry(new double[]{52.5250871,13.3672133}), new Properties("Berlin Hbf","foo", POIType.TRAIN_STATION)));
        res.add(new POI(new Geometry(new double[]{52.5264776,13.3130937}), new Properties("LAGESO","LAGESO", POIType.RECEPTION_CENTER)));
        res.add(new POI(new Geometry(new double[]{52.5101912,13.373077}), new Properties("Riz","Sleep well", POIType.ACCOMMODATION)));
        return res;
    }
}
