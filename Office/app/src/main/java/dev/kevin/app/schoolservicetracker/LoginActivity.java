package dev.kevin.app.schoolservicetracker;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import org.json.JSONObject;

import dev.kevin.app.schoolservicetracker.libs.ApiManager;
import dev.kevin.app.schoolservicetracker.libs.AppConstants;
import dev.kevin.app.schoolservicetracker.libs.AppHelper;
import dev.kevin.app.schoolservicetracker.libs.CallbackWithResponse;
import dev.kevin.app.schoolservicetracker.libs.Session;
import dev.kevin.app.schoolservicetracker.models.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout tlUsername, tlPassword;
    EditText txtUsername, txtPassword;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Session.get(this,"user",null) != null){
            Intent intent = new Intent(this,SchoolListActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        tlUsername = findViewById(R.id.tlUsername);
        tlPassword = findViewById(R.id.tlPassword);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);

        findViewById(R.id.btnSignIn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        int err = 0;
        if(username.equals("")){
            tlUsername.setError("Please enter a valid username");
            err++;
        }

        if(password.equals("")){
            tlPassword.setError("Please enter a valid password");
            err++;
        }

        if(err == 0){
            tlUsername.setError(null);
            tlPassword.setError(null);

            processLogin(username,password);
        }
    }

    private void processLogin(String username, String password) {

        String url = AppConstants.DOMAIN + "jjglogin/{username}/{password}";
        url = url.replace("{username}",AppHelper.urlEncode(username));
        url = url.replace("{password}",AppHelper.urlEncode(password));

        ApiManager.execute(this, url, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                ApiResponse r = gson.fromJson(response.toString(),ApiResponse.class);
                if(r.user == null){
                    tlUsername.setError("Login failed! please check username and password");
                    txtUsername.setText("");
                    txtPassword.setText("");
                }else{
                    Session.set(LoginActivity.this,"user",gson.toJson(r.user));
                    Intent intent = new Intent(LoginActivity.this,SchoolListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private class ApiResponse{
        private User user;

        public ApiResponse(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }
}
