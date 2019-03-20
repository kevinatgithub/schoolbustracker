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
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import dev.kevin.app.schoolbustrackeradmin.libs.ApiManager;
import dev.kevin.app.schoolbustrackeradmin.libs.AppConstants;
import dev.kevin.app.schoolbustrackeradmin.libs.CallbackWithResponse;
import dev.kevin.app.schoolbustrackeradmin.models.APIResponse_Buses;
import dev.kevin.app.schoolbustrackeradmin.models.Bus;

public class BusesActivity extends AppCompatActivity {

    ListView lv_drivers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buses);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        lv_drivers = findViewById(R.id.lv_drivers);
        lv_drivers.setOnItemClickListener(new ItemClicked());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        fetchDrivers();

    }

    private void fetchDrivers() {
        String url = AppConstants.DOMAIN + "buses";
        ApiManager.execute(this,url,new DriversLoaded());
    }

    private class DriversLoaded implements CallbackWithResponse{

        @Override
        public void execute(JSONObject response) {
            Gson gson = new Gson();
            APIResponse_Buses apiResponseBuses = gson.fromJson(response.toString(),APIResponse_Buses.class);
            ArrayList<Bus> buses = apiResponseBuses.getBuses();
            ArrayList<String> arrayListBuses = new ArrayList<>();
            for(Bus bus : buses){
                arrayListBuses.add(bus.getBus_no() + " - " + bus.getDriver());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplication(),android.R.layout.simple_list_item_1, arrayListBuses);
            lv_drivers.setAdapter(adapter);
        }
    }

    public void OnClick_newBus(View view){
        Intent intent = new Intent(getApplicationContext(),BusesFormActivity.class);
        startActivity(intent);
    }

    private class ItemClicked implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String strValue = (String) lv_drivers.getItemAtPosition(position);
            String[] parts = strValue.split(" - ");
            Intent intent = new Intent(getApplicationContext(), BusDetailsActivity.class);
            intent.putExtra("bus_no",parts[0]);
            intent.putExtra("driver",parts[1]);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}