package app.kevin.dev.schoolservicetrackerparent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import app.kevin.dev.schoolservicetrackerparent.lib.ApiManager;
import app.kevin.dev.schoolservicetrackerparent.lib.AppConstants;
import app.kevin.dev.schoolservicetrackerparent.lib.AppHelper;
import app.kevin.dev.schoolservicetrackerparent.lib.Callback;
import app.kevin.dev.schoolservicetrackerparent.lib.CallbackWithResponse;
import app.kevin.dev.schoolservicetrackerparent.lib.ConfirmDialogHelper;
import app.kevin.dev.schoolservicetrackerparent.lib.Session;
import app.kevin.dev.schoolservicetrackerparent.models.ApiResponse;
import app.kevin.dev.schoolservicetrackerparent.models.Parent;
import app.kevin.dev.schoolservicetrackerparent.models.Vehicle;

public class VehiclesListActivity extends AppCompatActivity {

    Gson gson = new Gson();
    Button btnHomeNotSet;
    TextView txtHomeNotSet;
    ListView lvVehicles;
    FloatingActionButton fabNewVehicle;
    Parent parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles_list);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        lvVehicles = findViewById(R.id.lvVehicles);

        txtHomeNotSet = findViewById(R.id.lblHomeNotSet);
        btnHomeNotSet = findViewById(R.id.btnHomeNotSet);
        fabNewVehicle = findViewById(R.id.fabAddVehicle);

        lvVehicles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                ConfirmDialogHelper.confirm(VehiclesListActivity.this, "Remove Vehicle", "Remove vehiccle from list?", new Callback() {
                    @Override
                    public void execute() {
                        if(parent.getVehicles().get(i) != null){
                            parent.getVehicles().remove(i);
                            Session.set(VehiclesListActivity.this,"parent",gson.toJson(parent));
                            if(parent.getVehicles() != null){
                                if(parent.getVehicles().size() != 0){
                                    loadVehicles();
                                }
                            }
                        }else{
                            Toast.makeText(VehiclesListActivity.this, "A problem occured while removing vehicle from list", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnHomeNotSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehiclesListActivity.this,SelectHomeLocation.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.fabAddVehicle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehiclesListActivity.this,ScanQRActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.fabChangeHomeLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehiclesListActivity.this,SelectHomeLocation.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
        finish();
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onPostResume() {
        super.onPostResume();
        String strParent = Session.get(this,"parent",null);

        if(strParent == null){
            txtHomeNotSet.setVisibility(View.VISIBLE);
            btnHomeNotSet.setVisibility(View.VISIBLE);
            fabNewVehicle.setVisibility(View.INVISIBLE);
        }else{
            txtHomeNotSet.setVisibility(View.INVISIBLE);
            btnHomeNotSet.setVisibility(View.INVISIBLE);
            fabNewVehicle.setVisibility(View.VISIBLE);

            parent = gson.fromJson(strParent,Parent.class);

            if(parent.getVehicles() != null){
                if(parent.getVehicles().size() != 0){
                    loadVehicles();
                }
            }
        }
    }

    ArrayList<Vehicle> vehicles = new ArrayList<>();

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
                    ArrayList<String> vehicles = new ArrayList<>();
                    for(Vehicle v: apiResponse.getVehicles()){
                        if(v != null){
                            vehicles.add(v.getPlate_no() + " - " + v.getModel() + " - " + v.getDriver());
                        }
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(VehiclesListActivity.this,android.R.layout.simple_list_item_1,vehicles);
                    lvVehicles.setAdapter(arrayAdapter);
                }
            }
        });

    }
}
