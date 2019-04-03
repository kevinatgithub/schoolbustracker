package dev.kevin.app.schoolservicetracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import dev.kevin.app.schoolservicetracker.libs.ApiManager;
import dev.kevin.app.schoolservicetracker.libs.AppConstants;
import dev.kevin.app.schoolservicetracker.libs.CallbackWithResponse;

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener  {

    MapView mapView;
    MapboxMap map;
    LatLng latLng;
    String school;

    FloatingActionButton fab1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,AppConstants.MAPBOX_ACCESS_TOKEN);
        setContentView(R.layout.activity_select_location);

        Intent intent = getIntent();
        school = intent.getStringExtra("school");

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        fab1 = findViewById(R.id.fab_btn);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSchool();
            }
        });

        findViewById(R.id.fab_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        map = mapboxMap;
        mapboxMap.setStyle(Style.SATELLITE_STREETS);
        mapboxMap.addOnMapClickListener(this);
        refreshMapCamera(14.584468,121.045721);
    }

    private void refreshMapCamera(double lat, double lng){
        if(map == null){
            return;
        }
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(16)
                .build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(position),5000);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        map.clear();
        map.addMarker(new MarkerOptions().setTitle("School Location").setPosition(new LatLng(point.getLatitude(),point.getLongitude())));
        latLng = point;

        fab1.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void registerSchool() {
        String URL = AppConstants.DOMAIN + "school/{name}/{lat}/{lng}";
        try {
            URL = URL.replace("{name}",URLEncoder.encode(school, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        URL = URL.replace("{lat}",latLng.getLatitude() + "");
        URL = URL.replace("{lng}",latLng.getLongitude() + "");

        ApiManager.execute(this, URL, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                Toast.makeText(SelectLocationActivity.this, "School has been Registered!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
