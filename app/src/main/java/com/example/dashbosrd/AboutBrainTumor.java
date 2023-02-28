package com.example.dashbosrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class AboutBrainTumor extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    String userNameGlobal;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_brain_tumor);

        setUserNameGlobally();

        toolBarFunctionalities();

        youtubeVideo("O1WWUPMpsaw");

        chipNavigationBarAboutBrainTumor();
    }

//                                           Related Methods

    public void setUserNameGlobally() {
        SharedPreferences prefs = getSharedPreferences("UserInfo",
                MODE_PRIVATE);
        userNameGlobal = prefs.getString("userName", "null");
    }

    public void youtubeVideo(String videoId) {
        try {
            YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view1);
            getLifecycle().addObserver(youTubePlayerView);

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_SHORT).show();
        }
    }

    public void toolBarFunctionalities() {
        //Button aboutBrainTumorToolBarBack = findViewById(R.id.aboutBrainTumorToolBarBackId);


    }


    public void chipNavigationBarAboutBrainTumor() {
        chipNavigationBar = findViewById(R.id.bottomNavbarAboutBrainTumorId);
        chipNavigationBar.setItemSelected(R.id.aboutId, true);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.aboutId:
                        //startActivity(new Intent(AboutBrainTumor.this, AboutBrainTumor.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.detectId:
                        startActivity(new Intent(AboutBrainTumor.this, BrainTumorDetect.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.treatmentId:
                        startActivity(new Intent(AboutBrainTumor.this, BrainTumorTreatment.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AboutBrainTumor.this, MainActivity.class));
        //overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}