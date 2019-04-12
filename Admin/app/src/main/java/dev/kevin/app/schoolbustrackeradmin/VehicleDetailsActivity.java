package dev.kevin.app.schoolbustrackeradmin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.zxing.WriterException;

import org.json.JSONObject;
import org.w3c.dom.Text;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import dev.kevin.app.schoolbustrackeradmin.libs.ApiManager;
import dev.kevin.app.schoolbustrackeradmin.libs.AppConstants;
import dev.kevin.app.schoolbustrackeradmin.libs.Callback;
import dev.kevin.app.schoolbustrackeradmin.libs.CallbackWithResponse;
import dev.kevin.app.schoolbustrackeradmin.libs.Session;
import dev.kevin.app.schoolbustrackeradmin.models.User;
import dev.kevin.app.schoolbustrackeradmin.models.Vehicle;
import retrofit2.http.GET;

public class VehicleDetailsActivity extends AppCompatActivity {

    Gson gson = new Gson();
    Vehicle vehicle;
    String school_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String strVehicle = intent.getStringExtra("vehicle");
        vehicle = gson.fromJson(strVehicle,Vehicle.class);

        TextView txtBusNo = findViewById(R.id.txtPlateNo);
        TextView txtDriver = findViewById(R.id.txtDriver);
        TextView txtModel = findViewById(R.id.txtModel);
        TextView txtContactNumber = findViewById(R.id.txtContactNo);

        txtBusNo.setText(vehicle.getPlate_no());
        txtDriver.setText(vehicle.getDriver());
        txtModel.setText(vehicle.getModel());
        txtContactNumber.setText(vehicle.getContact_no());

        ImageView imgQr = findViewById(R.id.imgQr);
        Bitmap bitmap;

        User user = gson.fromJson(Session.get(this,"user",null),User.class);
        school_id = user.getSchool_id()+"";

        Toast.makeText(this, school_id+"-" +vehicle.getId(), Toast.LENGTH_SHORT).show();
        QRGEncoder qrgEncoder = new QRGEncoder(school_id+"-" +vehicle.getId(), null, QRGContents.Type.TEXT, 200);

        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.encodeAsBitmap();
            // Setting Bitmap to ImageView
            imgQr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Toast.makeText(this, "Error QR", Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.btnRemoveVehicle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = AppConstants.DOMAIN + "vehicledelete/{school_id}/{id}";
                url = url.replace("{school_id}",school_id);
                url = url.replace("{id}",vehicle.getId()+"");
                ApiManager.execute(VehicleDetailsActivity.this,url,new RemoveComplete());
            }
        });
    }

    private class RemoveComplete implements CallbackWithResponse{

        @Override
        public void execute(JSONObject response) {
            Toast.makeText(VehicleDetailsActivity.this, "Vehicle has been removed", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void OnClick_Done(View view){
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
