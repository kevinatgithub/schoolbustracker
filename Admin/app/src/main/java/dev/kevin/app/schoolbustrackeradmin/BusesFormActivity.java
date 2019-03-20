package dev.kevin.app.schoolbustrackeradmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONObject;

import dev.kevin.app.schoolbustrackeradmin.libs.ApiManager;
import dev.kevin.app.schoolbustrackeradmin.libs.AppConstants;
import dev.kevin.app.schoolbustrackeradmin.libs.CallbackWithResponse;

public class BusesFormActivity extends AppCompatActivity {

    EditText txtBusNo;
    EditText txtDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buses_form);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        txtBusNo = findViewById(R.id.txtBusNo);
        txtDriver = findViewById(R.id.txtDriver);

    }

    public void OnClick_SaveBus(View view){

        String bus_no = txtBusNo.getText().toString();
        String driver = txtDriver.getText().toString();

        if(bus_no.equals("") || driver.equals("")){
            Toast.makeText(this, "Please enter Bus No. and Driver Name", Toast.LENGTH_SHORT).show();
            return;
        }

        submitBusDetailsToApi(bus_no,driver);
    }

    private void submitBusDetailsToApi(String bus_no, String driver) {
        String url = AppConstants.DOMAIN + "bus/" + bus_no + "/" + driver;
        ApiManager.execute(this,url,new BusDetailsSaved());
    }

    private class BusDetailsSaved implements CallbackWithResponse{

        @Override
        public void execute(JSONObject response) {
            Toast.makeText(BusesFormActivity.this, "Bus has been registered", Toast.LENGTH_SHORT).show();
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