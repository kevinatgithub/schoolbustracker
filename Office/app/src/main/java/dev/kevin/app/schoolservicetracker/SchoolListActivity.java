package dev.kevin.app.schoolservicetracker;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import dev.kevin.app.schoolservicetracker.libs.ApiManager;
import dev.kevin.app.schoolservicetracker.libs.AppConstants;
import dev.kevin.app.schoolservicetracker.libs.Callback;
import dev.kevin.app.schoolservicetracker.libs.CallbackWithResponse;
import dev.kevin.app.schoolservicetracker.libs.ConfirmDialogHelper;
import dev.kevin.app.schoolservicetracker.libs.Session;
import dev.kevin.app.schoolservicetracker.models.School;

public class SchoolListActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    LinearLayout lvHint;
    ListView lvSchools;
    Gson gson = new Gson();
    FloatingActionButton fabRegister;
    School[] schools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_list);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        lvHint = findViewById(R.id.lvHint);
        lvSchools = findViewById(R.id.lvSchools);
        lvSchools.setOnItemClickListener(this);
        fabRegister = findViewById(R.id.fabRegisterSchool);
        fabRegister.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ConfirmDialogHelper.confirm(this, "Sign-out", "Do you wan't to signout?", new Callback() {
            @Override
            public void execute() {
                signout();
            }
        });
        return super.onOptionsItemSelected(item);
    }

    private void signout() {
        Session.delete(this,"user");
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        Toast.makeText(this, "You have succesfully signed-out", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String URL = AppConstants.DOMAIN + "schools";
        ApiManager.execute(this, URL, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
                schools = apiResponse.getSchools();

                if(schools.length == 0){
                    lvHint.setVisibility(View.VISIBLE);
                }else{
                    lvHint.setVisibility(View.GONE);
                }

                ArrayList<String> schoolNameArrayList = new ArrayList<>();
                for(School school :apiResponse.getSchools()){
                    if(school.getName() != null){
                        schoolNameArrayList.add(school.getName());
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(SchoolListActivity.this,android.R.layout.simple_list_item_1, schoolNameArrayList);
                lvSchools.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,RegisterSchoolActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(),PreviewSchoolActivity.class);
        intent.putExtra("strSchool",gson.toJson(schools[position]));
        startActivity(intent);
    }

    private class ApiResponse{
        School[] schools;

        public ApiResponse(School[] schools) {
            this.schools = schools;
        }

        public School[] getSchools() {
            return schools;
        }

        public void setSchools(School[] schools) {
            this.schools = schools;
        }
    }
}
