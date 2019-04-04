package dev.kevin.app.schoolbustrackeradmin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
import dev.kevin.app.schoolbustrackeradmin.libs.Session;
import dev.kevin.app.schoolbustrackeradmin.models.ApiResponse;
import dev.kevin.app.schoolbustrackeradmin.models.MapMarkerOptions;
import dev.kevin.app.schoolbustrackeradmin.models.School;
import dev.kevin.app.schoolbustrackeradmin.models.User;
import dev.kevin.app.schoolbustrackeradmin.models.Vehicle;

public class VehicleLocatorActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView mapView;
    MapboxMap map;
    Icon icon,schoolIcon;
    ArrayList<Vehicle> vehicles = new ArrayList<>();
    Gson gson = new Gson();
    User user;
    ArrayList<MapMarkerOptions> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,AppConstants.MAPBOX_ACCESS_TOKEN);
        setContentView(R.layout.activity_vehicle_locator);

        String strUser = Session.get(this,"user",null);
        user = gson.fromJson(strUser,User.class);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        IconFactory iconFactory = IconFactory.getInstance(this);
        icon = iconFactory.fromResource(R.drawable.ic_school_bus);
        schoolIcon = iconFactory.fromResource(R.drawable.school);

        FloatingActionButton fab = findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        map = mapboxMap;
        mapboxMap.setStyle(Style.TRAFFIC_DAY);
        School school = user.getSchool();
        double lat = Double.parseDouble(school.getLat());
        double lng = Double.parseDouble(school.getLng());
        refreshMapCamera(lat, lng);
        mapboxMap.addMarker(new MarkerOptions().setPosition(new LatLng(lat,lng)).setIcon(schoolIcon).setTitle(school.getName()));
        fetchBusesCoordinates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void fetchBusesCoordinates() {
        String url = AppConstants.DOMAIN + "vehicles/"+user.getSchool_id();
        ApiManager.execute(this,url,new VehiclesLoaded());
    }

    private class VehiclesLoaded implements CallbackWithResponse{

        @Override
        public void execute(JSONObject response) {
            ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
            vehicles = apiResponse.getVehicles();

            for(Vehicle vehicle: vehicles){
                MapMarkerOptions opt = null;
                int i = 0;
                for(MapMarkerOptions marker: markers){
                    if(marker.getVehicle().getPlate_no().equals(vehicle.getPlate_no())){
                        opt = marker;
                        if(map.getMarkers().size() > i){
                            Marker m = map.getMarkers().get(i+1);
                            m.setPosition(new LatLng(vehicle.getLat(),vehicle.getLng()));
                            m.setTitle(vehicle.getPlate_no() + " - " +vehicle.getDriver());
                        }
                    }
                    i++;
                }

                if(opt == null){
                    opt = new MapMarkerOptions(vehicle);
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
                    },3000);
        }
    }

    private void refreshMapCamera(double latitude, double longtitude){
        if(map == null){
            return;
        }
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(latitude, longtitude))
                .zoom(16)
                .tilt(20)
                .build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(position),5000);
    }




}
