package dev.kevin.app.schoolservicetracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
import java.util.ArrayList;

import dev.kevin.app.schoolservicetracker.libs.ApiManager;
import dev.kevin.app.schoolservicetracker.libs.AppConstants;
import dev.kevin.app.schoolservicetracker.libs.AppHelper;
import dev.kevin.app.schoolservicetracker.libs.CallbackWithResponse;
import dev.kevin.app.schoolservicetracker.models.GeocodingResult;
import dev.kevin.app.schoolservicetracker.models.School;

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener  {

    Gson gson = new Gson();
    TextInputLayout tlAddress;
    EditText txtAddress;
    MapView mapView;
    MapboxMap map;
    LatLng latLng;
    School school;
    String schoolName,license_no,telephone_no,enableParentTracking;
    CardView cvHint;

    FloatingActionButton fab1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,AppConstants.MAPBOX_ACCESS_TOKEN);
        setContentView(R.layout.activity_select_location);

        tlAddress = findViewById(R.id.tlAddress);
        txtAddress = findViewById(R.id.txtAddress);
        txtAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String value = v.getText().toString();
                if(value == null){
                    return false;
                }
                if(value.isEmpty()){
                    return false;
                }

                geoCodeAddress(value);
                return false;
            }
        });

        Intent intent = getIntent();
        String strSchool = intent.getStringExtra("school");
        if(strSchool != null){
            school = gson.fromJson(strSchool,School.class);
            latLng = new LatLng(Double.parseDouble(school.getGeo().getLat()),Double.parseDouble(school.getGeo().getLng()));
        }
        schoolName = intent.getStringExtra("schoolName");
        license_no = intent.getStringExtra("license_no");
        telephone_no = intent.getStringExtra("telephone_no");
        enableParentTracking = intent.getStringExtra("enable_parent_tracking");

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        fab1 = findViewById(R.id.fab_btn);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(school != null){
                    updateSchool();
                }else{
                    registerSchool();
                }
            }
        });

        findViewById(R.id.fab_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cvHint = findViewById(R.id.cvHint);
        if(latLng == null){
            findViewById(R.id.imgCloseHint).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cvHint.setVisibility(View.GONE);
                }
            });
        }
    }

    private void geoCodeAddress(String value) {
        String url = AppConstants.MAP_QUEST_DIRECTION_URL;
        url = url.replace("[LOCATION]",AppHelper.urlEncode(value));

        ApiManager.execute(this, url, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
                if(apiResponse.results != null){
                    if(apiResponse.results.size() > 0){
                        applyToMap(apiResponse);
                    }else{
                        Toast.makeText(SelectLocationActivity.this, "Can't find address 2", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SelectLocationActivity.this, "Can't find address 1", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void applyToMap(ApiResponse apiResponse) {
        GeocodingResult result = apiResponse.getResults().get(0);
        GeocodingResult.Location location = result.getLocations()[0];
        GeocodingResult.LatLng latLng = location.getLatLng();
        refreshMapCamera(latLng.getLat(),latLng.getLng());
        map.clear();
        map.addMarker(new MarkerOptions().setTitle("School Location").setPosition(new LatLng(latLng.getLat(),latLng.getLng())));

        fab1.setVisibility(View.VISIBLE);
    }

    private class ApiResponse{
        private ArrayList<GeocodingResult> results;

        public ApiResponse(ArrayList<GeocodingResult> results) {
            this.results = results;
        }

        public ArrayList<GeocodingResult> getResults() {
            return results;
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        map = mapboxMap;
        mapboxMap.setStyle(Style.SATELLITE_STREETS);
        mapboxMap.addOnMapClickListener(this);
        if(school != null){
            refreshMapCamera(latLng.getLatitude(),latLng.getLongitude());
            mapboxMap.addMarker(new MarkerOptions().setTitle("School Location").setPosition(latLng));
        }else{
            refreshMapCamera(14.584468,121.045721);
        }
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
        String URL = AppConstants.DOMAIN + "school/{name}/{license_no}/{telephone_no}/{lat}/{lng}/{enable_parent_tracking}";
        URL = URL.replace("{name}",AppHelper.urlEncode(schoolName));
        URL = URL.replace("{license_no}",AppHelper.urlEncode(license_no));
        URL = URL.replace("{telephone_no}",AppHelper.urlEncode(telephone_no));
        URL = URL.replace("{lat}",latLng.getLatitude() + "");
        URL = URL.replace("{lng}",latLng.getLongitude() + "");
        URL = URL.replace("{enable_parent_tracking}",enableParentTracking);

        ApiManager.execute(this, URL, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                Toast.makeText(SelectLocationActivity.this, "School has been Registered!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateSchool() {
        String URL = AppConstants.DOMAIN + "schoolupdate/{id}/{name}/{license_no}/{telephone_no}/{lat}/{lng}/{enable_parent_tracking}";
        URL = URL.replace("{id}",String.valueOf(school.getId()));
        URL = URL.replace("{name}",AppHelper.urlEncode(schoolName));
        URL = URL.replace("{license_no}",AppHelper.urlEncode(license_no));
        URL = URL.replace("{telephone_no}",AppHelper.urlEncode(telephone_no));
        URL = URL.replace("{lat}",latLng.getLatitude() + "");
        URL = URL.replace("{lng}",latLng.getLongitude() + "");
        URL = URL.replace("{enable_parent_tracking}",enableParentTracking);

        ApiManager.execute(this, URL, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                Toast.makeText(SelectLocationActivity.this, "School has been updated!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
