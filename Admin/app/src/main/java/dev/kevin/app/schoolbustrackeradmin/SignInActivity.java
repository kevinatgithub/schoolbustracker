package dev.kevin.app.schoolbustrackeradmin;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import dev.kevin.app.schoolbustrackeradmin.libs.ApiManager;
import dev.kevin.app.schoolbustrackeradmin.libs.AppConstants;
import dev.kevin.app.schoolbustrackeradmin.libs.CallbackWithResponse;
import dev.kevin.app.schoolbustrackeradmin.libs.Session;
import dev.kevin.app.schoolbustrackeradmin.models.User;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, CallbackWithResponse {

    TextInputLayout tlUserID, tlPassword;
    EditText txtUserId, txtPassword;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        tlUserID = findViewById(R.id.tlUserID);
        tlPassword = findViewById(R.id.tlPassword);
        txtUserId = findViewById(R.id.txtUserID);
        txtPassword = findViewById(R.id.txtPassword);

        findViewById(R.id.btnSignIn).setOnClickListener(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        String strUser = Session.get(this,"user",null);
        if(strUser != null){
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        int err = 0;
        if(txtUserId.getText().equals("")){
            tlUserID.setError("Please enter email address");
            err++;
        }

        if(txtPassword.getText().equals("")){
            tlPassword.setError("Please enter password");
            err++;
        }

        if(err == 0){
            String userID = txtUserId.getText().toString();
            String password = txtPassword.getText().toString();

            String URL = AppConstants.DOMAIN + "login/{userID}/{password}";
            URL = URL.replace("{userID}", userID.trim());
            URL = URL.replace("{password}",password);

            ApiManager.execute(this,URL,this);
        }
    }

    @Override
    public void execute(JSONObject response) {
        ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
        if(apiResponse.status.equals("ok")){
            String json = gson.toJson(apiResponse.getUser());
            Session.set(this,"user",json);
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }else{
            tlUserID.setError("Login failed, check email and password");
        }
    }

    private class ApiResponse{

        private String status;
        private User user;

        public ApiResponse(String status, User user) {
            this.status = status;
            this.user = user;
        }

        public String getStatus() {
            return status;
        }

        public User getUser() {
            return user;
        }
    }
}
