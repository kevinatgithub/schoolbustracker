package dev.kevin.app.schoolbustrackerclient;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONObject;
import org.w3c.dom.Text;

import dev.kevin.app.schoolbustrackerclient.libs.ApiManager;
import dev.kevin.app.schoolbustrackerclient.libs.AppConstants;
import dev.kevin.app.schoolbustrackerclient.libs.Callback;
import dev.kevin.app.schoolbustrackerclient.libs.CallbackWithResponse;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager mLocationManager;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    private static final long MIN_TIME_BW_UPDATES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,0);
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
    }

    private void askForPermission(String permission, Integer requestCode) {
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
        TextView textView = findViewById(R.id.txt1);
        textView.setText(location.getLatitude() + " , " + location.getLongitude());

        String strLat = location.getLatitude()+"".replace("\\.","+");
        String strLng = location.getLongitude()+"".replace("\\.","+");

        String url = AppConstants.DOMAIN+"move2/1/"+strLat+"/"+strLng;

        ApiManager.execute(this, url, Request.Method.GET, null, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                Toast.makeText(MainActivity.this, "Moved", Toast.LENGTH_SHORT).show();
            }
        }, new Callback() {
            @Override
            public void execute() {
                Toast.makeText(MainActivity.this, "Error in Api Access", Toast.LENGTH_SHORT).show();
            }
        });
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
}
