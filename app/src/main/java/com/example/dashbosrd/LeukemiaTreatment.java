package com.example.dashbosrd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class LeukemiaTreatment extends AppCompatActivity {

    //----------------------- Bottom Navigation --------------------
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leukemia_treatment);

        //----------------------------------- Bottom Navigation (start)--------------------------------------


        chipNavigationBar = findViewById(R.id.bottomNavbarLeukemiaTreatmentId);
        chipNavigationBar.setItemSelected(R.id.treatmentId, true); //change
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.aboutId:
                        startActivity(new Intent(LeukemiaTreatment.this, AboutLeukemia.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.detectId:
                        startActivity(new Intent(LeukemiaTreatment.this, LeukemiaDetect.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.treatmentId:
                        ///startActivity(new Intent(LeukemiaTreatment.this, LeukemiaTreatment.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                }
            }
        });
        //----------------------------------- Bottom Navigation (end)--------------------------------------
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LeukemiaTreatment.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}