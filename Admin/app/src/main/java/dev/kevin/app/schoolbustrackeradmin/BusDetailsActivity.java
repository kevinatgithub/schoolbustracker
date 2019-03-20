package dev.kevin.app.schoolbustrackeradmin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.zxing.WriterException;

import org.json.JSONObject;
import org.w3c.dom.Text;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import dev.kevin.app.schoolbustrackeradmin.libs.ApiManager;
import dev.kevin.app.schoolbustrackeradmin.libs.AppConstants;
import dev.kevin.app.schoolbustrackeradmin.libs.CallbackWithResponse;

public class BusDetailsActivity extends AppCompatActivity {

    String bus_no;
    String driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        bus_no = intent.getStringExtra("bus_no");
        driver = intent.getStringExtra("driver");

        TextView txtBusNo = findViewById(R.id.txtBusNo);
        TextView txtDriver = findViewById(R.id.txtDriver);

        txtBusNo.setText(bus_no);
        txtDriver.setText(driver);

        ImageView imgQr = findViewById(R.id.imgQr);
        Bitmap bitmap;

        QRGEncoder qrgEncoder = new QRGEncoder(bus_no + "-" + driver, null, QRGContents.Type.TEXT, 200);

        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.encodeAsBitmap();
            // Setting Bitmap to ImageView
            imgQr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Toast.makeText(this, "Error QR", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnClick_RemoveBus(View view){
        String url = AppConstants.DOMAIN + "removebus/"+bus_no;
        ApiManager.execute(this,url,new RemoveComplete());
    }

    private class RemoveComplete implements CallbackWithResponse{

        @Override
        public void execute(JSONObject response) {
            Toast.makeText(BusDetailsActivity.this, "Bus has been removed", Toast.LENGTH_SHORT).show();
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
