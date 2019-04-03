package dev.kevin.app.schoolservicetracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;

import dev.kevin.app.schoolservicetracker.libs.ApiManager;
import dev.kevin.app.schoolservicetracker.libs.AppConstants;
import dev.kevin.app.schoolservicetracker.libs.CallbackWithResponse;
import dev.kevin.app.schoolservicetracker.models.School;
import dev.kevin.app.schoolservicetracker.models.User;

public class PreviewSchoolActivity extends AppCompatActivity implements OnMapReadyCallback{

    School school;
    Gson gson = new Gson();

    MapView mapView;
    MapboxMap map;
    TextView lblSchool;
    ListView lvAdmins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,AppConstants.MAPBOX_ACCESS_TOKEN);
        setContentView(R.layout.activity_preview_school);

        Intent intent = getIntent();
        String strSchool = intent.getStringExtra("strSchool");
        school = gson.fromJson(strSchool,School.class);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        lblSchool = findViewById(R.id.lblSchool);

        lblSchool.setText(school.getName());

        lvAdmins = findViewById(R.id.lvAdmins);

        findViewById(R.id.btnRegisterUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1  = new Intent(getApplicationContext(),RegisterAdminActivity.class);
                intent1.putExtra("school_id",school.getId()+"");
                startActivity(intent1);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadSchoolAdmins();
    }

    private void loadSchoolAdmins() {
        String URL = AppConstants.DOMAIN + "admins/" + school.getId();
        ApiManager.execute(this, URL, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
                ArrayList<String> strAdmins = new ArrayList<>();
                for(User u: apiResponse.getUsers()){
                    strAdmins.add(u.getName());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(PreviewSchoolActivity.this,android.R.layout.simple_list_item_1,strAdmins);
                lvAdmins.setAdapter(arrayAdapter);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        map = mapboxMap;
        mapboxMap.setStyle(Style.SATELLITE_STREETS);
        refreshMapCamera(Double.parseDouble(school.getGeo().getLat()),Double.parseDouble(school.getGeo().getLng()));
        mapboxMap.addMarker(new MarkerOptions().setPosition(new LatLng(Double.parseDouble(school.getGeo().getLat()),Double.parseDouble(school.getGeo().getLng()))));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void refreshMapCamera(double lat, double lng){
        if(map == null){
            return;
        }
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(16)
                .build();

        map.setCameraPosition(position);

//        map.animateCamera(CameraUpdateFactory.newCameraPosition(position),5000);
    }

    public void register(View view){
//        Intent intent = new Intent(getApplicationContext(),)
    }

    public void back(View view){
        finish();
    }

    private class ApiResponse{
        private User[] users;

        public ApiResponse(User[] users) {
            this.users = users;
        }

        public User[] getUsers() {
            return users;
        }
    }
}
