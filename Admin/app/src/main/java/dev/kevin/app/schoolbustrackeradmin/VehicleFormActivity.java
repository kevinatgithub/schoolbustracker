package dev.kevin.app.schoolbustrackeradmin;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import dev.kevin.app.schoolbustrackeradmin.libs.ApiManager;
import dev.kevin.app.schoolbustrackeradmin.libs.AppConstants;
import dev.kevin.app.schoolbustrackeradmin.libs.AppHelper;
import dev.kevin.app.schoolbustrackeradmin.libs.CallbackWithResponse;
import dev.kevin.app.schoolbustrackeradmin.libs.Session;
import dev.kevin.app.schoolbustrackeradmin.models.User;

public class VehicleFormActivity extends AppCompatActivity {

    TextInputLayout tlPlateNo,tlModel,tlDriverName,tlContactNumber;
    EditText txtPlateNo,txtModel,txtDriverName,txtContactNumber;
    int school_id;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles_form);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        String txtUser = Session.get(this,"user",null);
        User user = gson.fromJson(txtUser,User.class);
        school_id = user.getSchool_id();

        tlPlateNo = findViewById(R.id.tlPlateNo);
        tlModel = findViewById(R.id.tlModel);
        tlDriverName = findViewById(R.id.tlDriverName);
        tlContactNumber = findViewById(R.id.tlContactNo);

        txtPlateNo = findViewById(R.id.txtPlateNo);
        txtModel = findViewById(R.id.txtModel);
        txtDriverName = findViewById(R.id.txtDriverName);
        txtContactNumber = findViewById(R.id.txtContactNo);


    }

    public void OnClick_SaveVehicle(View view){

        String plateNo = txtPlateNo.getText().toString();
        String model = txtModel.getText().toString();
        String driverName = txtDriverName.getText().toString();
        String contactNumber = txtContactNumber.getText().toString();

        int err = 0;

        if(plateNo.equals("")){
            tlPlateNo.setError("Please provide the Vehicle Plate Number");
            err++;
        }

        if(model.equals("")){
            tlModel.setError("Please provide the vehicle model");
            err++;
        }

        if(driverName.equals("")){
            tlDriverName.setError("The Driver Name is required");
            err++;
        }

        if(contactNumber.equals("")){
            tlContactNumber.setError("Please provide the driver's contact number");
            err++;
        }

        if(err == 0){
            submitVehicleDetailsToApi(plateNo,model,driverName,contactNumber);
        }

    }

    private void submitVehicleDetailsToApi(String plateNo, String model, String driverName, String contactNumber) {
        String url = AppConstants.DOMAIN + "vehicle/{school_id}/{plate_no}/{model}/{driver}/{contact_no}";
        url = url.replace("{school_id}",school_id+"");
        url = url.replace("{plate_no}", AppHelper.urlEncode(plateNo));
        url = url.replace("{model}",AppHelper.urlEncode(model));
        url = url.replace("{driver}",AppHelper.urlEncode(driverName));
        url = url.replace("{contact_no}",AppHelper.urlEncode(contactNumber));

        ApiManager.execute(this,url,new VehicleDetailsSaved());
    }

    private class VehicleDetailsSaved implements CallbackWithResponse{

        @Override
        public void execute(JSONObject response) {
            Toast.makeText(VehicleFormActivity.this, "Vehicle has been registered", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void OnClick_Cancel(View view){
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
