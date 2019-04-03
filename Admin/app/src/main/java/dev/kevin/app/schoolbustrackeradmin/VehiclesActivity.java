package dev.kevin.app.schoolbustrackeradmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import dev.kevin.app.schoolbustrackeradmin.libs.ApiManager;
import dev.kevin.app.schoolbustrackeradmin.libs.AppConstants;
import dev.kevin.app.schoolbustrackeradmin.libs.CallbackWithResponse;
import dev.kevin.app.schoolbustrackeradmin.libs.Session;
import dev.kevin.app.schoolbustrackeradmin.models.ApiResponse;
import dev.kevin.app.schoolbustrackeradmin.models.User;
import dev.kevin.app.schoolbustrackeradmin.models.Vehicle;

public class VehiclesActivity extends AppCompatActivity {

    ListView lv_vehicles;
    User user;
    Gson gson = new Gson();
    String school_id;
    ArrayList<Vehicle> vehicles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);

        String strUser = Session.get(this,"user",null);
        User user = gson.fromJson(strUser,User.class);
        school_id = user.getSchool_id()+"";

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        lv_vehicles = findViewById(R.id.lv_vehicles);
        lv_vehicles.setOnItemClickListener(new ItemClicked());

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        fetchDrivers();

    }

    private void fetchDrivers() {
        String url = AppConstants.DOMAIN + "vehicles/"+school_id;
        ApiManager.execute(this,url,new DriversLoaded());
    }

    private class DriversLoaded implements CallbackWithResponse{

        @Override
        public void execute(JSONObject response) {
            ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
            vehicles = apiResponse.getVehicles();
            ArrayList<String> plateNumbers = new ArrayList<>();
            for(Vehicle v: apiResponse.getVehicles()){
                plateNumbers.add(v.getPlate_no() + " - " + v.getModel());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(VehiclesActivity.this,android.R.layout.simple_list_item_1, plateNumbers);
            lv_vehicles.setAdapter(adapter);
        }
    }

    public void OnClick_newVehicle(View view){
        Intent intent = new Intent(getApplicationContext(),VehicleFormActivity.class);
        startActivity(intent);
    }

    private class ItemClicked implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Vehicle vehicle = vehicles.get(position);
            Intent intent = new Intent(getApplicationContext(), VehicleDetailsActivity.class);
            intent.putExtra("vehicle",gson.toJson(vehicle));
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }


}
