package app.kevin.dev.schoolservicetrackerparent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import app.kevin.dev.schoolservicetrackerparent.adapters.VehicleAdapter;
import app.kevin.dev.schoolservicetrackerparent.lib.ApiManager;
import app.kevin.dev.schoolservicetrackerparent.lib.AppConstants;
import app.kevin.dev.schoolservicetrackerparent.lib.AppHelper;
import app.kevin.dev.schoolservicetrackerparent.lib.CallbackWithResponse;
import app.kevin.dev.schoolservicetrackerparent.lib.DirectionHelper;
import app.kevin.dev.schoolservicetrackerparent.lib.Session;
import app.kevin.dev.schoolservicetrackerparent.models.ApiResponse;
import app.kevin.dev.schoolservicetrackerparent.models.Parent;
import app.kevin.dev.schoolservicetrackerparent.models.Vehicle;

public class HomeActivity extends AppCompatActivity {

    Parent parent;
    Gson gson = new Gson();
    ListView lvVehicles;
    ImageView imgEmpty;
    TextView lblEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        lvVehicles = findViewById(R.id.lvVehicles);
        imgEmpty = findViewById(R.id.imgEmpty);
        lblEmpty = findViewById(R.id.lblEmpty);

        findViewById(R.id.fabRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLoadVehicles();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        attemptLoadVehicles();

    }

    private void attemptLoadVehicles() {
        String json = Session.get(this,"parent",null);

        if(json == null){
            imgEmpty.setVisibility(View.VISIBLE);
            lblEmpty.setVisibility(View.VISIBLE);
        }else{
            parent = gson.fromJson(json,Parent.class);

            if(parent.getVehicles() != null){
                if(parent.getVehicles().size() > 0){
                    loadVehicles();
                }
            }

        }
    }

    private void loadVehicles() {
        String params = "";
        for(String qr: parent.getVehicles()){
            params += qr+",";
        }
        String url = AppConstants.DOMAIN + "parent/vehicles/"+ AppHelper.urlEncode(params);
        ApiManager.execute(this, url, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
                if(apiResponse.getVehicles() != null){
                    ArrayList<Vehicle> vehicles = new ArrayList<>();
                    for(Vehicle v: apiResponse.getVehicles()){
                        if(v != null){
                            vehicles.add(v);
                        }
                    }
                    listVehicles(vehicles);
                }
            }
        });

    }

    private void listVehicles(ArrayList<Vehicle> vehicles) {

        VehicleAdapter vehicleAdapter = new VehicleAdapter(this,vehicles);
        lvVehicles.setAdapter(vehicleAdapter);

        calculateDistanceAndTime(vehicles);
    }

    private void calculateDistanceAndTime(ArrayList<Vehicle> vehicles) {
        int i = 0;
        for(Vehicle v: vehicles){
            if(v == null){
                continue;
            }
            getDistance(i,v.getLat(), v.getLng(), parent.getLat(), parent.getLng(), new CallbackDistance() {
                @Override
                public void execute(int position,String distance, String time) {
                    View v = getViewByPosition(position,lvVehicles);
                    TextView lblDistance = v.findViewById(R.id.lblDistance);
                    lblDistance.setText(distance + " km");
                    TextView lblTime = v.findViewById(R.id.lblTime);
                    lblTime.setText(time + " mins");
                }
            });
            i++;
        }
    }

    private void getDistance(final int position, double pointA_lat, double pointA_lng, double pointB_lat, double pointB_lng, final CallbackDistance callbackDistance){
        DirectionHelper.getDistance(this, pointA_lat, pointA_lng, pointB_lat, pointB_lng, new DirectionHelper.DirectionHelperCallback() {
            @Override
            public void execute(DirectionHelper.Route route) {
                if(route != null){
                    String distance = String.valueOf(roundOff(route.getDistance()*1.60934));
                    String time = String.valueOf(roundOff(route.getTime() / 60));
                    callbackDistance.execute(position,distance,time);
                }
            }
        });
    }

    private double roundOff(double value){
        return Math.round(value * 100.0) / 100.00;
    }

    private interface CallbackDistance{
        public void execute(int position,String distance, String time);
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this,VehiclesListActivity.class);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }
}
