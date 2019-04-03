package dev.kevin.app.schoolservicetracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import dev.kevin.app.schoolservicetracker.libs.ApiManager;
import dev.kevin.app.schoolservicetracker.libs.AppConstants;
import dev.kevin.app.schoolservicetracker.libs.CallbackWithResponse;

public class RegisterSchoolActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout tlSchool;
    EditText txtSchool;
    Button btnRegister,btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_school);

        tlSchool = findViewById(R.id.tlSchool);
        txtSchool = findViewById(R.id.txtSchool);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(txtSchool.getText().toString().equals("")){
            tlSchool.setError("Please enter name of school");
            return;
        }
        String school = txtSchool.getText().toString();
        Intent i = new Intent(this,SelectLocationActivity.class);
        i.putExtra("school",school);
        startActivity(i);
        finish();
    }

}
