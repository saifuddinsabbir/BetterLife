package com.example.dashbosrd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class OnboardingScreen extends AppCompatActivity {

    ViewPager viewPager;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_onboarding_screen);


        viewPager = findViewById(R.id.viewPagerId);

        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.swipe1);
        imageList.add(R.drawable.swip2);
        imageList.add(R.drawable.swip5);
        imageList.add(R.drawable.three);
        imageList.add(R.drawable.one);

        MyAdapter myAdapter = new MyAdapter(imageList);
        viewPager.setAdapter(myAdapter);

        //lets start
        startButton = findViewById(R.id.startButtonId);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }
}