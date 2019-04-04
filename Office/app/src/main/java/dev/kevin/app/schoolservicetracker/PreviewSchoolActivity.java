package dev.kevin.app.schoolservicetracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import dev.kevin.app.schoolservicetracker.libs.ApiManager;
import dev.kevin.app.schoolservicetracker.libs.AppConstants;
import dev.kevin.app.schoolservicetracker.libs.AppHelper;
import dev.kevin.app.schoolservicetracker.libs.Callback;
import dev.kevin.app.schoolservicetracker.libs.CallbackWithResponse;
import dev.kevin.app.schoolservicetracker.libs.ConfirmDialogHelper;
import dev.kevin.app.schoolservicetracker.models.School;
import dev.kevin.app.schoolservicetracker.models.User;

public class PreviewSchoolActivity extends AppCompatActivity implements OnMapReadyCallback{

    School school;
    Gson gson = new Gson();

    MapView mapView;
    MapboxMap map;
    TextView lblSchool,lblLicenseNo,lblTelephoneNo;
    ListView lvAdmins;
    LinearLayout lvHint;
    User[] users;

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
        lblLicenseNo = findViewById(R.id.lblLicenseNo);
        lblLicenseNo.setText("License No. " +school.getLicense_no());
        lblTelephoneNo = findViewById(R.id.lblTelephoneNo);
        lblTelephoneNo.setText("Telephone No." +school.getTelephone_no());

        lvAdmins = findViewById(R.id.lvAdmins);
        lvAdmins.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final User user = users[position];
                ConfirmDialogHelper.confirm(PreviewSchoolActivity.this, "Delete Administrator", "Delete School Administrator?", new Callback() {
                    @Override
                    public void execute() {
                        deleteUser(user);
                    }
                });
            }
        });

        lvHint = findViewById(R.id.lvHint);

        findViewById(R.id.btnRegisterUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1  = new Intent(getApplicationContext(),RegisterAdminActivity.class);
                intent1.putExtra("school_id",school.getId()+"");
                startActivity(intent1);
            }
        });

        findViewById(R.id.btnUpdateSchool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),UpdateSchoolActivity.class);
                i.putExtra("schoolName",gson.toJson(school));
                startActivity(i);
                finish();
            }
        });
        
        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmDialogHelper.confirm(PreviewSchoolActivity.this, "Delete School", "Do you want to delete schoolName?", new Callback() {
                    @Override
                    public void execute() {
                        deleteSchool();
                    }
                });
            }
        });
    }

    private void deleteUser(User user) {
        String user_id = AppHelper.urlEncode(user.getUser_id());
        String url = AppConstants.DOMAIN + "admindelete/"+user.getUser_id();
        ApiManager.execute(this, url, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                Toast.makeText(PreviewSchoolActivity.this, "School Administrator has been deleted", Toast.LENGTH_SHORT).show();
                loadSchoolAdmins();
            }
        });
    }

    private void deleteSchool() {
        String url = AppConstants.DOMAIN + "schooldelete/"+school.getId();
        ApiManager.execute(this, url, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                Toast.makeText(PreviewSchoolActivity.this, "School Information has been deleted", Toast.LENGTH_SHORT).show();
                finish();
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
                users = apiResponse.getUsers();
                if(apiResponse.getUsers().length == 0){
                    lvHint.setVisibility(View.VISIBLE);
                }else{
                    lvHint.setVisibility(View.GONE);
                }
                ArrayList<String> strAdmins = new ArrayList<>();
                int i = 1;
                for(User u: apiResponse.getUsers()){
                    strAdmins.add(i + ". " +u.getName());
                    i++;
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
