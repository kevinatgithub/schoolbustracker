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

import com.google.gson.Gson;
import com.google.zxing.WriterException;

import org.json.JSONObject;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import dev.kevin.app.schoolbustrackeradmin.libs.ApiManager;
import dev.kevin.app.schoolbustrackeradmin.libs.AppConstants;
import dev.kevin.app.schoolbustrackeradmin.libs.CallbackWithResponse;
import dev.kevin.app.schoolbustrackeradmin.models.Vehicle;

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

        txtBusNo.setText(vehicle.getPlate_no());
        txtDriver.setText(vehicle.getDriver());

        ImageView imgQr = findViewById(R.id.imgQr);
        Bitmap bitmap;

        QRGEncoder qrgEncoder = new QRGEncoder(vehicle.getPlate_no(), null, QRGContents.Type.TEXT, 200);

        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.encodeAsBitmap();
            // Setting Bitmap to ImageView
            imgQr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Toast.makeText(this, "Error QR", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnClick_RemoveVehicle(View view){
        String url = AppConstants.DOMAIN + "vehicledelete/{school_id}/{id}";
        url = url.replace("{school_id}",school_id);
        url = url.replace("{id}",vehicle.getId()+"");
        ApiManager.execute(this,url,new RemoveComplete());
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
