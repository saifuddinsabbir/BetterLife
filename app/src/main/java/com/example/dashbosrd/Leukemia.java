package com.example.dashbosrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.dashbosrd.MainActivity;

import com.google.android.material.navigation.NavigationView;

public class Leukemia extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

//-------------------------------------------------------------- D R A W E R - L A Y O U T (start)---------------------------------------------------------
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
//-------------------------------------------------------------- D R A W E R - L A Y O U T (end)---------------------------------------------------------

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leukemia);


//-------------------------------------------------------------- D R A W E R - L A Y O U T (start)---------------------------------------------------------
        //-------------Hooks----------------------------
        drawerLayout = findViewById(R.id.drawerLayoutId);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toolbar);
        //-------------Toolbar-------------------------------
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Leukemia");
        toolbar.setSubtitle("");
        //---------------Navigation Drawer Menu------------------
        //-----Hide or show items
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_profile);
        menu.findItem(R.id.nav_logout);
        //-------------------------------------------------------
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //-------------------------------------------------------
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_leukemia);
//-------------------------------------------------------------- D R A W E R - L A Y O U T (end)---------------------------------------------------------

    }

//-------------------------------------------------------------- D R A W E R - L A Y O U T (start)---------------------------------------------------------
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(Leukemia.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(Leukemia.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                break;

            case R.id.nav_leukemia:
                //startActivity(new Intent(Leukemia.this, Leukemia.class));
                break;

            case R.id.nav_brain_tumor:
                startActivity(new Intent(Leukemia.this, BrainTumorDetect.class));
                break;

            case R.id.nav_profile:
                startActivity(new Intent(Leukemia.this, UserProfile.class));
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