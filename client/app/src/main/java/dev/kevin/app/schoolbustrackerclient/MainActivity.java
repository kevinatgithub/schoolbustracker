package dev.kevin.app.schoolbustrackerclient;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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

import dev.kevin.app.schoolbustrackerclient.libs.ApiManager;
import dev.kevin.app.schoolbustrackerclient.libs.AppConstants;
import dev.kevin.app.schoolbustrackerclient.libs.AppHelper;
import dev.kevin.app.schoolbustrackerclient.libs.Session;
import dev.kevin.app.schoolbustrackerclient.model.School;
import dev.kevin.app.schoolbustrackerclient.model.Vehicle;

public class MainActivity  extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    MapView mapView;
    MapboxMap map;
    Icon icon;
    private Marker marker;
    Gson gson = new Gson();
    Vehicle vehicle;
    School school;

    protected LocationManager mLocationManager;

    protected static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    protected static final long MIN_TIME_BW_UPDATES = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,AppConstants.MAPBOX_ACCESS_TOKEN);
        setContentView(R.layout.activity_main);


        IconFactory iconFactory = IconFactory.getInstance(this);
        icon = iconFactory.fromResource(R.drawable.schoolbus);

        String qrcode = Session.get(this,"qrcode",null);

        if(qrcode == null){
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Intent intent = getIntent();
        String strVehicle = intent.getStringExtra("vehicle");
        vehicle = gson.fromJson(strVehicle,Vehicle.class);
        String strSchool = Session.get(this,"school",null);
        school = gson.fromJson(strSchool,School.class);

        beginGettingLocation();


    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        map = mapboxMap;
        mapboxMap.setStyle(Style.TRAFFIC_DAY);


        if(school != null){
            double lat,lng;
            lat = Double.parseDouble(school.getGeo().getLat());
            lng = Double.parseDouble(school.getGeo().getLng());
            mapboxMap.addMarker(new MarkerOptions().setTitle(school.getName()).setPosition(new LatLng(lat,lng)));
        }

        if(vehicle != null){
            double lat,lng;
            lat = vehicle.getLat();
            lng = vehicle.getLng();
            displayMarkerOnMap(lat,lng);
        }

        refreshMapCamera(vehicle.getLat(),vehicle.getLng());
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
//                .tilt(20)
                .build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(position),5000);
    }

    public void beginGettingLocation(){
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,0);
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
    }

    public void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(final Location location) {

        refreshMapCamera(location.getLatitude(),location.getLongitude());
        displayMarkerOnMap(location.getLatitude(),location.getLongitude());
        reportDeviceLocation(location);
        processDistanceFromSchool(location);
    }

    private void reportDeviceLocation(Location location) {
        String qrcode = Session.get(this,"qrcode",null);
        String[] parts = qrcode.split("\\-");
        String school_id = parts[0];
        String plate_no = parts[1];

        String url = AppConstants.DOMAIN+"vehiclelocation/"+school_id+"/"+vehicle.getId()+"/"+location.getLatitude()+"/"+location.getLongitude();

        ApiManager.execute(this, url, null);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void displayMarkerOnMap(double lat, double lng) {
        if(map == null){
            return;
        }
        if(marker == null){
            map.addMarker(new MarkerOptions().setTitle("Your Location").setPosition(new LatLng(lat,lng))).setIcon(icon);
            ArrayList<Marker> markers = (ArrayList<Marker>) map.getMarkers();
            marker = markers.get(1);
        }else{
            marker.setPosition(new LatLng(lat,lng));
        }
    }

    private void processDistanceFromSchool(Location location) {
        double lat = Double.parseDouble(school.getGeo().getLat());
        double lng = Double.parseDouble(school.getGeo().getLng());

        Double distance = distance(lat,location.getLatitude(),lng,location.getLongitude(),0,0);

        String status = vehicle.getStatus() != null ? vehicle.getStatus() : "";
        if(distance < 100 && !status.equals("In School")){
            updateVehicleStatus(vehicle.getId(),"In School");
        }else if(!status.equals("In Transit") && !status.equals("Destress")){
            updateVehicleStatus(vehicle.getId(),"In Transit");
        }
    }

    private void updateVehicleStatus(int id,String status){
        String url = AppConstants.DOMAIN + "vehiclestatus/{school_id}/{id}/{status}";
        url = url.replace("{school_id}",school.getId()+"");
        url = url.replace("{id}",id+"");
        url = url.replace("{status}",AppHelper.urlEncode(status));

        ApiManager.execute(this, url, null);
    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
