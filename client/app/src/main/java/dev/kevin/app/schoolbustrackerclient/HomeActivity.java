package dev.kevin.app.schoolbustrackerclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import dev.kevin.app.schoolbustrackerclient.libs.ApiManager;
import dev.kevin.app.schoolbustrackerclient.libs.AppConstants;
import dev.kevin.app.schoolbustrackerclient.libs.CallbackWithResponse;
import dev.kevin.app.schoolbustrackerclient.libs.Session;
import dev.kevin.app.schoolbustrackerclient.model.School;
import dev.kevin.app.schoolbustrackerclient.model.Vehicle;

public class HomeActivity extends AppCompatActivity {

    TextView lblPlateNumber, lblPlateNumber2, lblModel, lblModel2, lblDriver, lblDriver2, lblContactNumber, lblContactNumber2, lblLoading;
    Gson gson = new Gson();
    Vehicle vehicle;
    School school;
    Button btnStartTracker;

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
            lblPlateNumber2 = findViewById(R.id.lblPlateNumber2);
            lblModel = findViewById(R.id.lblModel);
            lblModel2 = findViewById(R.id.lblModel2);
            lblDriver = findViewById(R.id.lblDriver);
            lblDriver2 = findViewById(R.id.lblDriver2);
            lblContactNumber = findViewById(R.id.lblContactNumber);
            lblContactNumber2 = findViewById(R.id.lblContactNumber2);
            lblLoading = findViewById(R.id.lblLoading);

            loadVehicleDetails(qrcode);

            btnStartTracker = findViewById(R.id.btnStartTracker);
            btnStartTracker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(vehicle == null){
                        return;
                    }
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
        final String school_id = parts[0];
        String plate_no = parts[1];

        String url = AppConstants.DOMAIN + "vehicle/"+school_id+"/"+plate_no;

        ApiManager.execute(this, url, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
                vehicle = apiResponse.getVehicle();
                school = apiResponse.getSchool();
                Session.set(getApplicationContext(),"school",gson.toJson(school));

                if(vehicle == null){
                    lblLoading.setText("Please scan QR to get vehicle details");
                    btnStartTracker.setEnabled(false);
                    return;
                }
                lblPlateNumber.setText(vehicle.getPlate_no());
                lblPlateNumber2.setVisibility(View.VISIBLE);
                lblModel.setText(vehicle.getModel());
                lblModel2.setVisibility(View.VISIBLE);
                lblDriver.setText(vehicle.getDriver());
                lblDriver2.setVisibility(View.VISIBLE);
                lblContactNumber.setText(vehicle.getContact_no());
                lblContactNumber2.setVisibility(View.VISIBLE);
                lblLoading.setVisibility(View.GONE);
            }
        });
    }

    private class ApiResponse{
        private Vehicle vehicle;
        private School school;

        public ApiResponse(Vehicle vehicle, School school) {
            this.vehicle = vehicle;
            this.school = school;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }

        public School getSchool() {
            return school;
        }
    }
}
