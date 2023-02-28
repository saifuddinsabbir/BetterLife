package com.example.dashbosrd;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class AboutLeukemia extends AppCompatActivity {
    //----------------------- Bottom Navigation --------------------
    ChipNavigationBar chipNavigationBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_leukemia);

        chipNavigationBarAboutLeukemia();
    }

    public void toolBarFunctionalities() {
        ImageView aboutLeukemiaToolBarBack = findViewById(R.id.aboutLeukemiaToolBarBackId);

        aboutLeukemiaToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutLeukemia.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
    }


    public void chipNavigationBarAboutLeukemia() {
        //----------------------------------- Bottom Navigation (start)--------------------------------------

        chipNavigationBar = findViewById(R.id.bottomNavbarAboutLeukemiaId);
        chipNavigationBar.setItemSelected(R.id.aboutId, true);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.aboutId:
                        //startActivity(new Intent(AboutLeukemia.this, AboutLeukemia.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.detectId:
                        startActivity(new Intent(AboutLeukemia.this, LeukemiaDetect.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.treatmentId:
                        startActivity(new Intent(AboutLeukemia.this, LeukemiaTreatment.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                }
            }
        });
        //----------------------------------- Bottom Navigation (end)--------------------------------------
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AboutLeukemia.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}

