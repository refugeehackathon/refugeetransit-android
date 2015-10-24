package de.refugeehackathon.transit;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

import de.refugeehackathon.transit.data.api.ApiModule;
import de.refugeehackathon.transit.data.api.PoiService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class MainActivity extends AppCompatActivity {

    private MapView mMapView;

    private PoiService mPoiService;

    public static final GeoPoint BERLIN = new GeoPoint(52.516667, 13.383333);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initPoiService();

        mMapView = (MapView) findViewById(R.id.mapview);
        setupMapView();

        addMarkers();
    }

    private int getDrawableForType(POIType type) {
        switch (type) {
            case ACCOMODATION:
                return R.drawable.ic_action_home;
            case TRAIN_STATION:
                return R.drawable.ic_action_train;
            default:
            case RECEPTION_CENTER:
                return R.drawable.ic_action_bell;
        }
    }

    private void addMarkers() {
        final DefaultResourceProxyImpl resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
        final ArrayList<OverlayItem> items = new ArrayList<>();

        final List<POI> pois = new POIProvider().getPOIS();

        for (POI poi : pois) {
            final GeoPoint currentLocation = new GeoPoint(poi.latitude, poi.longitude);
            final OverlayItem myLocationOverlayItem = new OverlayItem(poi.title, poi.description, currentLocation);
            final Drawable myCurrentLocationMarker = getResources().getDrawable(getDrawableForType(poi.type));
            myLocationOverlayItem.setMarker(myCurrentLocationMarker);
            items.add(myLocationOverlayItem);
        }

        ItemizedIconOverlay currentLocationOverlay = new ItemizedIconOverlay<>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        return true;
                    }

                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return true;
                    }
                }, resourceProxy);
        mMapView.getOverlays().add(currentLocationOverlay);
    }

    private void setupMapView() {
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);
        mMapView.setUseDataConnection(true);
        mMapView.setTileSource(TileSourceFactory.MAPQUESTOSM);

        final MapController mMapController = (MapController) mMapView.getController();
        mMapController.setZoom(13);
        mMapController.setCenter(BERLIN);

        fetchPois();
    }

    private void initPoiService() {
        RefugeeTransitApplication application = (RefugeeTransitApplication) getApplication();
        ApiModule apiModule = application.getApiModule();
        mPoiService = apiModule.providePoisService();
    }

    private void fetchPois() {
        Call<List<POI>> readPoisCall = mPoiService.readPois();
        readPoisCall.enqueue(new Callback<List<POI>>() {
            @Override
            public void onResponse(Response<List<POI>> response, Retrofit retrofit) {
                onReadPoisSuccess(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                new Exception(t);
            }
        });
    }

    private void onReadPoisSuccess(List<POI> pois) {
        // TODO Here you can use the POIs.
        Log.d(getClass().getName(), "POIs: " + pois);
    }

}
