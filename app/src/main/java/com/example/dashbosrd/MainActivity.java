package com.example.dashbosrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    TextView userNameTextView;
    ImageView userProfileImage;

    String userNameGlobal;

    CardView leukemiaCardView, brainTumorCardView, consultationCardView, medicineCardView, updateProfileCardView, feedbackCardView, nearbyHospitalsCardView,
            medicalRecordsCardView, ambulanceCardView, healthTipsCardView;

    SessionManager sessionManager;
    HashMap<String, String> userDetails;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUserNameGlobally();

        sessionManager = new SessionManager(this);
        userDetails = sessionManager.getUsersDetailFromSession();

        drawerLayoutMethod();

        leukemiaCardView = findViewById(R.id.leukemiaCardViewId);
        brainTumorCardView = findViewById(R.id.brainTumorCardViewId);
        consultationCardView = findViewById(R.id.consultationCardViewId);
        medicineCardView = findViewById(R.id.medicineCardViewId);
        updateProfileCardView = findViewById(R.id.updateProfileCardViewId);
        nearbyHospitalsCardView = findViewById(R.id.nearbyHospitalsCardViewId);
        feedbackCardView = findViewById(R.id.feedbackCardViewId);
        medicalRecordsCardView = findViewById(R.id.medicalRecordsCardViewId);
        ambulanceCardView = findViewById(R.id.ambulanceCardViewId);
        healthTipsCardView = findViewById(R.id.healthTipsCardViewId);
        userNameTextView = findViewById(R.id.userNameId);
        userProfileImage = findViewById(R.id.userProfileImage);

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserProfile.class));
            }
        });

        userNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserProfile.class));
            }
        });

        userNameTextView.setText("@" + userDetails.get(SessionManager.KEY_USERNAME));
        if(userDetails.get(SessionManager.KEY_DP) != null) {
            Picasso.get().load(userDetails.get(SessionManager.KEY_DP)).into(userProfileImage);
        }

        leukemiaCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AboutLeukemia.class));
                ////overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

         brainTumorCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutBrainTumor.class);
//                intent.putExtra("userName", userNameIntent);
                startActivity(intent);
                ////overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        consultationCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Consultation.class));
            }
        });

        medicineCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Medicine.class));
            }
        });

        updateProfileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserProfile.class);
//                intent.putExtra("userName", userNameIntent);
                startActivity(intent);
                ////overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        nearbyHospitalsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NearbyMaps.class));
            }
        });

        medicalRecordsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MedicalRecords.class));
            }
        });

        ambulanceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Ambulance.class));
            }
        });

        healthTipsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HealthTips.class));
            }
        });

        feedbackCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Feedback.class));
            }
        });
    }

    public void setUserNameGlobally() {
        SharedPreferences prefs = getSharedPreferences("UserInfo",
                MODE_PRIVATE);
        userNameGlobal = prefs.getString("userName", "null");
    }




















    //-------------------------------------------------------------- D R A W E R - L A Y O U T (start)---------------------------------------------------------
    public void drawerLayoutMethod () {
        //-------------Hooks----------------------------
        drawerLayout = findViewById(R.id.drawerLayoutId);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toolbar);
        //-------------Toolbar-------------------------------
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Home");
        toolbar.setSubtitle("");
        //---------------Navigation Drawer Menu------------------
        //-----Hide or show items
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_profile);
        menu.findItem(R.id.nav_logout);
        menu.findItem(R.id.nav_rate);
        //-------------------------------------------------------
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //-------------------------------------------------------
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this)
                    .setTitle("Exit")
                    .setMessage("Want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    })
                    .setNegativeButton("Cancel", null);

            builder.create();
            builder.show();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                ////overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                //startActivity(new Intent(MainActivity.this, MainActivity.class));
                break;

            case R.id.nav_leukemia:
                startActivity(new Intent(MainActivity.this, AboutLeukemia.class));
                //////overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;

            case R.id.nav_brain_tumor:
                startActivity(new Intent(MainActivity.this, AboutBrainTumor.class));
                //////overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;

            case R.id.nav_profile:
                Intent intent_nav_profile = new Intent(MainActivity.this, UserProfile.class);
//                intent_nav_profile.putExtra("userName", userNameIntent);
                startActivity(intent_nav_profile);
                //////overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;

            case R.id.nav_logout:
                SharedPreferences.Editor editor = getSharedPreferences("UserInfo",
                        MODE_PRIVATE).edit();
                editor.putString("userName", "null");
                editor.apply();

                Intent intent_nav_logout = new Intent(MainActivity.this, Login.class);
                startActivity(intent_nav_logout);
                ////overridePendingTransition(R.anim.slide_from_top, R.anim.slide_to_bottom);
                break;

            case R.id.nav_rate:
                Intent intent_nav_rating = new Intent(MainActivity.this, AppRating.class);
                startActivity(intent_nav_rating);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;

            case R.id.nav_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                String subject = "Android app: Better Life";

                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, R.string.share_value);
                startActivity(Intent.createChooser(intent, "Share with.."));
                Toast.makeText(getApplicationContext(), "Sharing..", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    //-------------------------------------------------------------- D R A W E R - L A Y O U T (end)---------------------------------------------------------
}