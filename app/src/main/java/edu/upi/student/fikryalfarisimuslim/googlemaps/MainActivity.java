package edu.upi.student.fikryalfarisimuslim.googlemaps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void run_OnClick(View v) {
        finish();
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
        Intent intent2 = new Intent(this, MapsActivity.class);
        startActivity(intent2);


    }
}
