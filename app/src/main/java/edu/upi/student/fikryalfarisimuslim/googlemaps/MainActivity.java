package edu.upi.student.fikryalfarisimuslim.googlemaps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "runandgo.message";
    EditText nama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nama = (EditText) findViewById(R.id.editText2);
    }

    public void run_OnClick(View v) {
        Intent intent2 = new Intent(this, Main2Activity.class);
        intent2.putExtra(EXTRA_MESSAGE,nama.getText().toString());
        startActivity(intent2);
        finish();

    }
}
