package dev.kevin.app.schoolservicetracker;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import dev.kevin.app.schoolservicetracker.libs.ApiManager;
import dev.kevin.app.schoolservicetracker.libs.AppConstants;
import dev.kevin.app.schoolservicetracker.libs.CallbackWithResponse;
import dev.kevin.app.schoolservicetracker.models.School;

public class RegisterAdminActivity extends AppCompatActivity {

    TextInputLayout tlUserID, tlName, tlPassword, tlConfirmPassword;
    EditText txtUserID, txtName, txtPassword, txtConfirmPassword;
    String school_id;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);

        Intent intent = getIntent();
        school_id = intent.getStringExtra("school_id");

        tlUserID = findViewById(R.id.tlUserID);
        tlName = findViewById(R.id.tlFullName);
        tlPassword = findViewById(R.id.tlPassword);
        tlConfirmPassword = findViewById(R.id.tlConfirmPassword);
        txtUserID = findViewById(R.id.txtUserID);
        txtName = findViewById(R.id.txtFullName);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfrmPassword);

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    registerAdmin();
            }
        });

        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForm();
            }
        });
    }

    private void clearForm() {
        txtUserID.setText("");
        txtName.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
    }

    private void registerAdmin() {
        int error = 0;
        if(txtUserID.getText().equals("")){
            tlUserID.setError("Please Enter User ID");
            error++;
        }
        if(txtName.getText().equals("")){
            tlName.setError("Please Enter Full Name");
            error++;
        }
        if(txtPassword.getText().equals("")){
            tlPassword.setError("Please enter password");
            error++;
        }
        String p1 = txtPassword.getText().toString();
        String p2 = txtConfirmPassword.getText().toString();
        if(!p1.equals(p2)){
            tlConfirmPassword.setError("Password did not match");
            error++;
        }
        if(error== 0){
            doRegister();
        }
    }

    private void doRegister() {
        String URL = AppConstants.DOMAIN + "register/admin/{school_id}/{user_id}/{password}/{name}";
        URL = URL.replace("{school_id}",school_id);
        URL = URL.replace("{user_id}",txtUserID.getText().toString());
        URL = URL.replace("{password}",txtPassword.getText().toString());
        URL = URL.replace("{name}",txtName.getText().toString());

        ApiManager.execute(this, URL, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
                if(apiResponse.getStatus().equals("success")){
                    Toast.makeText(RegisterAdminActivity.this, "School Administrator Registered", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    tlUserID.setError(apiResponse.getMessage());
                }
            }
        });
    }

    private class ApiResponse{
        private String status;
        private String message;

        public ApiResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
