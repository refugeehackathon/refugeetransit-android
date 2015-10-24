package de.refugeehackathon.transit;

import java.util.ArrayList;
import java.util.List;

public class POIProvider {

    public List<POI> getPOIS() {
        final List<POI> res = new ArrayList<>();
        res.add(new POI(POIType.TRAIN_STATION, new Geometry(new double[]{52.5250871,13.3672133}), new Properties("Berlin Hbf","foo")));
        res.add(new POI(POIType.RECEPTION_CENTER,new Geometry(new double[]{52.5264776,13.3130937}), new Properties("LAGESO","LAGESO")));
        res.add(new POI(POIType.ACCOMMODATION,new Geometry(new double[]{52.5101912,13.373077}), new Properties("Riz","Sleep well")));
        return res;
    }
}
