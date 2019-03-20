package dev.kevin.app.schoolbustrackeradmin;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import org.json.JSONObject;

import java.util.ArrayList;

import dev.kevin.app.schoolbustrackeradmin.libs.ApiManager;
import dev.kevin.app.schoolbustrackeradmin.libs.AppConstants;
import dev.kevin.app.schoolbustrackeradmin.libs.CallbackWithResponse;
import dev.kevin.app.schoolbustrackeradmin.models.APIResponse_Buses;
import dev.kevin.app.schoolbustrackeradmin.models.Bus;
import dev.kevin.app.schoolbustrackeradmin.models.MapMarkerOptions;

public class BusLocatorActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView mapView;
    Icon icon;
    ProgressBar progressBarLoading;
    ArrayList<Bus> buses = new ArrayList<>();
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,AppConstants.MAPBOX_ACCESS_TOKEN);
        setContentView(R.layout.activity_bus_locator);

        progressBarLoading = findViewById(R.id.progressBarLoading);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        IconFactory iconFactory = IconFactory.getInstance(this);
        icon = iconFactory.fromResource(R.drawable.ic_school_bus);

        FloatingActionButton fab = findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    MapboxMap map;

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        map = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS);

        refreshMapCamera();

        fetchBusesCoordinates();
    }

    private void fetchBusesCoordinates() {
        String url = AppConstants.DOMAIN + "buses";
        ApiManager.execute(this,url,new BusesLoaded());


    }

    ArrayList<MapMarkerOptions> markers = new ArrayList<>();

    private class BusesLoaded implements CallbackWithResponse{

        @Override
        public void execute(JSONObject response) {
            APIResponse_Buses apiResponse_buses = gson.fromJson(response.toString(),APIResponse_Buses.class);
            buses = apiResponse_buses.getBuses();
            for(Bus bus: buses){
                MapMarkerOptions opt = null;
                int i = 0;
                for(MapMarkerOptions marker: markers){
                    if(marker.getBus().getBus_no().equals(bus.getBus_no())){
                        opt = marker;
                        if(map.getMarkers().size() > i){
                            Marker m = map.getMarkers().get(i);
                            m.setPosition(new LatLng(bus.getLat(),bus.getLng()));
                            m.setTitle(bus.getBus_no() + " - " +bus.getDriver());
                        }
                    }
                    i++;
                }

                if(opt == null){
                    opt = new MapMarkerOptions(bus);
                    opt.getMarkerOptions().setIcon(icon);
                    markers.add(opt);
                    map.addMarker(opt.getMarkerOptions());
                }
            }

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            fetchBusesCoordinates();
                        }
                    },5000);
        }
    }

    private void refreshMapCamera(){
        if(map == null){
            return;
        }
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(14.584468, 121.045721))
                .zoom(11)
                .tilt(20)
                .build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(position),5000);
    }


}
