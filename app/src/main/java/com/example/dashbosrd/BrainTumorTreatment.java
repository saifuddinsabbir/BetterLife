package com.example.dashbosrd;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class BrainTumorTreatment extends AppCompatActivity {

    //----------------------- Bottom Navigation --------------------
    ChipNavigationBar chipNavigationBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain_tumor_treatment);

        chipNavigationBarAboutBrainTumor();
    }



    public void chipNavigationBarAboutBrainTumor() {
        //----------------------------------- Bottom Navigation (start)--------------------------------------

        chipNavigationBar = findViewById(R.id.bottomNavbarBrainTumorTreatmetId);
        chipNavigationBar.setItemSelected(R.id.treatmentId, true); //change
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.aboutId:
                        startActivity(new Intent(BrainTumorTreatment.this, AboutBrainTumor.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.detectId:
                        startActivity(new Intent(BrainTumorTreatment.this, BrainTumorDetect.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.treatmentId:
                        //startActivity(new Intent(BrainTumorTreatment.this, BrainTumorTreatment.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                }
            }
        });
        //----------------------------------- Bottom Navigation (end)--------------------------------------
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BrainTumorTreatment.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}