package com.example.dashbosrd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;
    Animation topAnimation, bottomAnimatio;
    ImageView logo, background;
    TextView title, slogan;
    LottieAnimationView lottieAnimationView;

    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Full Screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_splash_screen);



        //call animation method
        animation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("UserInfo",
                        MODE_PRIVATE);
                String userNameGlobal = prefs.getString("userName", "null");

//                onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
//                if (onBoardingScreen.getBoolean("firstTime", true)) {
//                    SharedPreferences.Editor editor = onBoardingScreen.edit();
//                    editor.putBoolean("firstTime", false);
//                    editor.apply();
//
//                    startActivity(new Intent(SplashScreen.this, OnBoarding.class));
//                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
////                    overridePendingTransition(R.anim.slide_from_top, R.anim.slide_to_bottom);
//                    finish();
//                }

                if(!userNameGlobal.equals("null")) {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    //overridePendingTransition(R.anim.slide_from_top, R.anim.slide_to_bottom);
                } else {
                    startActivity(new Intent(SplashScreen.this, OnBoarding.class));
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
//                    overridePendingTransition(R.anim.slide_from_top, R.anim.slide_to_bottom);
                    finish();
                }

//                startActivity(new Intent(SplashScreen.this, OnBoarding.class));
//                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                finish();

            }
        }, SPLASH_SCREEN);

    }


    public void animation() {
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_anomation);
        bottomAnimatio = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);


        lottieAnimationView = findViewById(R.id.lottieAnimationId);
        //logo = findViewById(R.id.logoId);
        title = findViewById(R.id.titleId);
        slogan = findViewById(R.id.sloganId);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(3000);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

//        lottieAnimationView.setAnimation(topAnimation);
//        title.setAnimation(anim);
//        slogan.setAnimation(anim);
//        title.setAnimation(bottomAnimatio);
//        slogan.setAnimation(bottomAnimatio);
    }

}