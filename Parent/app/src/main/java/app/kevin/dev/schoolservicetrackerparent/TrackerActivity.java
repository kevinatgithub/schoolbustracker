package app.kevin.dev.schoolservicetrackerparent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
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

import app.kevin.dev.schoolservicetrackerparent.lib.ApiManager;
import app.kevin.dev.schoolservicetrackerparent.lib.AppConstants;
import app.kevin.dev.schoolservicetrackerparent.lib.AppHelper;
import app.kevin.dev.schoolservicetrackerparent.lib.CallbackWithResponse;
import app.kevin.dev.schoolservicetrackerparent.lib.Session;
import app.kevin.dev.schoolservicetrackerparent.models.ApiResponse;
import app.kevin.dev.schoolservicetrackerparent.models.Parent;
import app.kevin.dev.schoolservicetrackerparent.models.Vehicle;

public class TrackerActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView mapView;
    MapboxMap map;
    Parent parent;
    Gson gson = new Gson();
    Icon vehicleIcon,parentIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, AppConstants.MAPBOX_ACCESS_TOKEN);
        setContentView(R.layout.activity_tracker);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        IconFactory iconFactory = IconFactory.getInstance(this);
        vehicleIcon = iconFactory.fromResource(R.drawable.ic_vehicle);
        parentIcon = iconFactory.fromResource(R.drawable.ic_parent);

        findViewById(R.id.fabBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        String json = Session.get(this,"parent",null);
        parent = gson.fromJson(json,Parent.class);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        map = mapboxMap;
        mapboxMap.setStyle(Style.TRAFFIC_DAY);
        if(parent != null){
            mapboxMap.addMarker(new MarkerOptions().setIcon(parentIcon).setPosition(new LatLng(parent.getLat(),parent.getLng())).setTitle("Your House"));
            refreshMapCamera(parent.getLat(),parent.getLng());
            loadVehicles();
        }
    }

    private void loadVehicles() {
        String ids = "";
        for(String qr: parent.getVehicles()){
            ids += qr+",";
        }
        String url = AppConstants.DOMAIN + "parent/vehicles/"+ AppHelper.urlEncode(ids);
        ApiManager.execute(this, url, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
                if(apiResponse.getVehicles() != null){
                    addVehiclesToMap(apiResponse.getVehicles());
                }

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                loadVehicles();
                            }
                        },3000);
            }
        });
    }

    private void addVehiclesToMap(ArrayList<Vehicle> vehicles) {
        if(map == null){
            return;
        }

        map.clear();
        map.addMarker(new MarkerOptions().setIcon(parentIcon).setPosition(new LatLng(parent.getLat(),parent.getLng())).setTitle("Your House"));


        for(Vehicle v: vehicles){
            if(v.getEnable_parent_tracking().equals("1")){
                map.addMarker(new MarkerOptions().setPosition(new LatLng(v.getLat(),v.getLng())).setIcon(vehicleIcon).setTitle(v.getPlate_no() + "\n" + v.getModel() + "\n" + v.getDriver()));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
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
