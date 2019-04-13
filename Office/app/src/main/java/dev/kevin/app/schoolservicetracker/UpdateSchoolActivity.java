package dev.kevin.app.schoolservicetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.google.gson.Gson;

import dev.kevin.app.schoolservicetracker.models.School;

public class UpdateSchoolActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout tlSchool,tlLicenseNo,tlTelephoneNo;
    EditText txtSchool,txtLicenseNo,txtTelephoneNo;
    Button btnRegister,btnCancel;
    Gson gson = new Gson();
    School school;
    Switch switchEnableParentTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_school);

        Toolbar toolbar = findViewById(R.id.app_bar);
        toolbar.setTitle("Update School Information");

        Intent i = getIntent();
        String strSchool = i.getStringExtra("schoolName");
        school = gson.fromJson(strSchool,School.class);

        tlSchool = findViewById(R.id.tlSchool);
        tlLicenseNo = findViewById(R.id.tlLicenseNumber);
        tlTelephoneNo = findViewById(R.id.tlTelephoneNo);
        txtSchool = findViewById(R.id.txtSchool);
        txtSchool.setText(school.getName());
        txtLicenseNo = findViewById(R.id.txtLicenseNo);
        txtLicenseNo.setText(school.getLicense_no());
        txtTelephoneNo = findViewById(R.id.txtTelephoneNo);
        txtTelephoneNo.setText(school.getTelephone_no());
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        switchEnableParentTracking = findViewById(R.id.switchEnableParentTracking);
        if(school.getEnable_parent_tracking() != null){
            if(school.getEnable_parent_tracking().equals("1")){
                switchEnableParentTracking.setChecked(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int err = 0;
        if(txtSchool.getText().toString().equals("")){
            tlSchool.setError("Please enter name of schoolName");
            err++;
        }

        if(txtLicenseNo.getText().toString().equals("")){
            tlLicenseNo.setError("Please enter School's License Number");
            err++;
        }

        if(txtTelephoneNo.getText().toString().equals("")){
            tlTelephoneNo.setError("Please enter School's Telephone Number");
            err++;
        }

        if(err > 0){
            return;
        }

        String name = txtSchool.getText().toString();
        String licenseNo = txtLicenseNo.getText().toString();
        String telephoneNo = txtTelephoneNo.getText().toString();
        String enableParentTracking = "1";
        if(!switchEnableParentTracking.isChecked()){
            enableParentTracking = "0";
        }
        Intent i = new Intent(this,SelectLocationActivity.class);
        i.putExtra("school",gson.toJson(school));
        i.putExtra("schoolName",name);
        i.putExtra("license_no",licenseNo);
        i.putExtra("telephone_no",telephoneNo);
        i.putExtra("enable_parent_tracking",enableParentTracking);
        startActivity(i);
        finish();
    }

}
