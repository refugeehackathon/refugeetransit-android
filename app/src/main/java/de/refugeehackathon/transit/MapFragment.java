package de.refugeehackathon.transit;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
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

public class MapFragment extends Fragment {

    @Bind(R.id.mapview)
    MapView mMapView;

    @Bind(R.id.poiTitle)
    TextView infoTitle;

    @Bind(R.id.poiDescription)
    TextView poiDescription;

    @Bind(R.id.poiInfoContainer)
    ViewGroup infoContainer;

    private PoiService mPoiService;
    public static final GeoPoint BERLIN = new GeoPoint(52.516667, 13.383333);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.content_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        initPoiService();
        setupMapView();

        super.onActivityCreated(savedInstanceState);
    }

    private void addMarkers(List<POI> pois) {
        final DefaultResourceProxyImpl resourceProxy = new DefaultResourceProxyImpl(getActivity().getApplicationContext());
        final ArrayList<OverlayItem> items = new ArrayList<>();

        for (POI poi : pois) {
            double latitude = poi.geometry.getLatitude();
            double longitude = poi.geometry.getLongitude();
            final GeoPoint currentLocation = new GeoPoint(latitude, longitude);
            Properties properties = poi.properties;
            String title = "";
            String description = "";
            POIType poitype = POIType.UNKNOWN;
            if (properties != null) {
                title = properties.name;
                description = properties.description;
                poitype = properties.poitype;
            }
            final OverlayItem myLocationOverlayItem = new OverlayItem(title, description, currentLocation);

            final Drawable myCurrentLocationMarker = getResources().getDrawable(getDrawableForType(poitype));

            myLocationOverlayItem.setMarker(myCurrentLocationMarker);
            items.add(myLocationOverlayItem);
        }

        ItemizedIconOverlay currentLocationOverlay = new ItemizedIconOverlay<>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        infoContainer.setVisibility(View.VISIBLE);
                        String title = item.getTitle();
                        if (TextUtils.isEmpty(title)) {
                            infoTitle.setVisibility(View.GONE);

                        } else {
                            infoTitle.setText(Html.fromHtml(title));
                            infoTitle.setVisibility(View.VISIBLE);
                        }
                        String description = item.getSnippet();
                        if (TextUtils.isEmpty(description)) {
                            poiDescription.setVisibility(View.GONE);
                        } else {
                            poiDescription.setText(Html.fromHtml(description));
                            poiDescription.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }

                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return true;
                    }
                }, resourceProxy);

        mMapView.getOverlays().add(currentLocationOverlay);
        mMapView.getOverlays().add(new Overlay(getContext()) {
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


    private int getDrawableForType(POIType type) {
        switch (type) {
            case ACCOMMODATION:
                return R.drawable.ic_action_home;
            case TRAIN_STATION:
                return R.drawable.ic_action_train;
            case RECEPTION_CENTER:
                return R.drawable.ic_action_bell;
            case UNKNOWN:
            default:
                return R.drawable.ic_action_location;
        }
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



    private void initPoiService() {
        RefugeeTransitApplication application = (RefugeeTransitApplication) getActivity().getApplication();
        ApiModule apiModule = application.getApiModule();
        mPoiService = apiModule.providePoisService();
    }


}
