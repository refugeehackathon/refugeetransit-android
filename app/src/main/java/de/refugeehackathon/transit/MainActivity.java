package de.refugeehackathon.transit;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.refugeehackathon.transit.data.api.ApiModule;
import de.refugeehackathon.transit.data.api.PoiService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.mapview)
    MapView mMapView;

    @Bind(R.id.poiTitle)
    TextView infoTitle;

    @Bind(R.id.poiDescription)
    TextView poiDescription;

    @Bind(R.id.poiInfoContainer)
    ViewGroup infoContainer;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawer_navigation)
    NavigationView navigationView;

    private PoiService mPoiService;

    public static final GeoPoint BERLIN = new GeoPoint(52.516667, 13.383333);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        initPoiService();
        setupMapView();

        mMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                infoContainer.setVisibility(View.GONE);
                return false;
            }
        });
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        final ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        super.onPostCreate(savedInstanceState);
    }

    private int getDrawableForType(POIType type) {
        switch (type) {
            case ACCOMMODATION:
                return R.drawable.ic_action_home;
            case TRAIN_STATION:
                return R.drawable.ic_action_train;
            default:
            case RECEPTION_CENTER:
                return R.drawable.ic_action_bell;
        }
    }

    private void addMarkers(List<POI> pois) {
        final DefaultResourceProxyImpl resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
        final ArrayList<OverlayItem> items = new ArrayList<>();

        for (POI poi : pois) {
            double latitude = poi.geometry.coordinates[0];
            double longitude = poi.geometry.coordinates[1];
            final GeoPoint currentLocation = new GeoPoint(latitude, longitude);
            Properties properties = poi.properties;
            String title = "";
            String description = "";
            if (properties != null) {
                title = properties.name;
                description = properties.description;
            }
            final OverlayItem myLocationOverlayItem = new OverlayItem(title, description, currentLocation);

            final Drawable myCurrentLocationMarker = getResources().getDrawable(getDrawableForType(poi.type));

            myLocationOverlayItem.setMarker(myCurrentLocationMarker);
            items.add(myLocationOverlayItem);
        }

        ItemizedIconOverlay currentLocationOverlay = new ItemizedIconOverlay<>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        infoContainer.setVisibility(View.VISIBLE);
                        infoTitle.setText(item.getTitle());
                        poiDescription.setText(item.getSnippet());
                        return true;
                    }

                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return true;
                    }
                }, resourceProxy);

        mMapView.getOverlays().add(currentLocationOverlay);
        mMapView.getOverlays().add(new Overlay(this) {
            @Override
            protected void draw(Canvas c, MapView osmv, boolean shadow) {

            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {
                infoContainer.setVisibility(View.GONE);
                return super.onSingleTapConfirmed(e, mapView);
            }
        });
    }

    private void setupMapView() {
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
                t.printStackTrace();
            }
        });
    }

    private void onReadPoisSuccess(List<POI> pois) {
        addMarkers(pois);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        return false;
    }
}
