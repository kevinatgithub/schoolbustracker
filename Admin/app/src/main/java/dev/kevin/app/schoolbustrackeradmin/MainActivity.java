package dev.kevin.app.schoolbustrackeradmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import dev.kevin.app.schoolbustrackeradmin.libs.Session;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnBuses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VehiclesActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnTracker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VehicleLocatorActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnSignOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Session.delete(getApplicationContext(),"user");
                Toast.makeText(MainActivity.this, "You have been signed-out", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
