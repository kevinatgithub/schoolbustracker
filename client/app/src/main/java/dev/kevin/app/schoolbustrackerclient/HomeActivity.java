package dev.kevin.app.schoolbustrackerclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import dev.kevin.app.schoolbustrackerclient.libs.ApiManager;
import dev.kevin.app.schoolbustrackerclient.libs.AppConstants;
import dev.kevin.app.schoolbustrackerclient.libs.CallbackWithResponse;
import dev.kevin.app.schoolbustrackerclient.libs.Session;
import dev.kevin.app.schoolbustrackerclient.model.Vehicle;

public class HomeActivity extends AppCompatActivity {

    TextView lblPlateNumber, lblModel, lblDriver, lblContactNumber;
    Gson gson = new Gson();
    Vehicle vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String qrcode = Session.get(this,"qrcode",null);

        if(qrcode == null || qrcode.equals("")){
            Intent intent = new Intent(this,ScanQRActivity.class);
            startActivity(intent);
            finish();
        }else{

            lblPlateNumber = findViewById(R.id.lblPlateNumber);
            lblModel = findViewById(R.id.lblModel);
            lblDriver = findViewById(R.id.lblDriver);
            lblContactNumber = findViewById(R.id.lblContactNumber);

            loadVehicleDetails(qrcode);
//        Toast.makeText(this, qrcode, Toast.LENGTH_SHORT).show();

            findViewById(R.id.btnStartTracker).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("vehicle",gson.toJson(vehicle));
                    startActivity(intent);
                }
            });

            findViewById(R.id.btnChangeDeviceIdentity).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),ScanQRActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    private void loadVehicleDetails(String qrcode) {
        String[] parts = qrcode.split("\\-");
        String school_id = parts[0];
        String plate_no = parts[1];

        String url = AppConstants.DOMAIN + "vehicle/"+school_id+"/"+plate_no;

        ApiManager.execute(this, url, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
                vehicle = apiResponse.vehicle;

                lblPlateNumber.setText(vehicle.getPlate_no());
                lblModel.setText(vehicle.getModel());
                lblDriver.setText(vehicle.getDriver());
                lblContactNumber.setText(vehicle.getContact_no());
            }
        });
    }

    private class ApiResponse{
        private Vehicle vehicle;

        public ApiResponse(Vehicle vehicle) {
            this.vehicle = vehicle;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }
    }
}
