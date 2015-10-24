package de.refugeehackathon.transit.data.api;

import java.util.List;

import de.refugeehackathon.transit.BuildConfig;
import de.refugeehackathon.transit.POI;
import retrofit.Call;
import retrofit.http.GET;

public interface PoiService {

    @GET(BuildConfig.API_POIS_READ_PATH)
    Call<List<POI>> readPois();

}
