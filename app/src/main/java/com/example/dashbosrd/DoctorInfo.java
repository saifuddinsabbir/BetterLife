package com.example.dashbosrd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class DoctorInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);

        String appointmentKey = getIntent().getStringExtra("appointmentKey");

        Toast.makeText(this, appointmentKey, Toast.LENGTH_SHORT).show();
    }
}