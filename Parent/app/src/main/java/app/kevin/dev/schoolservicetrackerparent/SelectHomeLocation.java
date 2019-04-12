package app.kevin.dev.schoolservicetrackerparent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.ArrayList;

import app.kevin.dev.schoolservicetrackerparent.lib.AppConstants;
import app.kevin.dev.schoolservicetrackerparent.lib.Callback;
import app.kevin.dev.schoolservicetrackerparent.lib.ConfirmDialogHelper;
import app.kevin.dev.schoolservicetrackerparent.lib.Session;
import app.kevin.dev.schoolservicetrackerparent.models.Parent;

public class SelectHomeLocation extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {

    MapView mapView;
    MapboxMap map;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, AppConstants.MAPBOX_ACCESS_TOKEN);
        setContentView(R.layout.activity_select_home_location);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        map = mapboxMap;
        map.setStyle(Style.SATELLITE_STREETS);
        mapboxMap.addOnMapClickListener(this);
        refreshMapCamera(14.600495,121.006784);
    }

    private void refreshMapCamera(double latitude, double longtitude){
        if(map == null){
            return;
        }
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(latitude, longtitude))
                .zoom(10)
                .tilt(20)
                .build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(position),5000);
    }

    @Override
    public boolean onMapClick(@NonNull final LatLng point) {

        ConfirmDialogHelper.confirm(this, "Select Home Location", "Are you sure this is your home location?", new Callback() {
            @Override
            public void execute() {
                String json = Session.get(SelectHomeLocation.this,"parent",null);
                ArrayList<String> vehicles = new ArrayList<>();
                if(json != null){
                    Parent p = gson.fromJson(json,Parent.class);
                    vehicles = p.getVehicles();
                }
                Parent parent = new Parent(point.getLatitude(),point.getLongitude(),vehicles);
                String str = gson.toJson(parent);
                Session.set(SelectHomeLocation.this,"parent",str);
                finish();

            }
        });
        return false;
    }
}
