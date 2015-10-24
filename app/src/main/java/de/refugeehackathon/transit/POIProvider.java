package de.refugeehackathon.transit;

import java.util.ArrayList;
import java.util.List;

public class POIProvider {

    public List<POI> getPOIS() {
        final List<POI> res = new ArrayList<>();
        res.add(new POI(POIType.TRAIN_STATION,52.5250871,13.3672133,"Berlin Hbf","foo"));
        res.add(new POI(POIType.RECEPTION_CENTER,52.5264776,13.3130937,"LAGESO","LAGESO"));
        res.add(new POI(POIType.ACCOMMODATION,52.5101912,13.373077,"Riz","Sleep well"));
        return res;
    }
}
