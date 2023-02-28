package com.example.dashbosrd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnBoarding extends AppCompatActivity {

    ViewPager onBoardingSlider;
    LinearLayout onBoardingDots;
    SliderAdapter sliderAdapter;
    Button onBoardingGetStartedButton, onBoardingSkipButton, onBoardingNext;
    Animation onBoardingGetStartedButtonAnimation, onBoardingNextAnimation;
    TextView[] dots;
    int currentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_on_boarding);

        //hooks
        onBoardingSlider = findViewById(R.id.onBoardingSliderId);
        onBoardingDots = findViewById(R.id.onBoardingDotsId);
        onBoardingGetStartedButton = findViewById(R.id.onBoardingGetStartedButtonId);
        onBoardingSkipButton = findViewById(R.id.onBoardingSkipButtonId);
        onBoardingNext = findViewById(R.id.onBoardingNextId);


        //call adapter
        sliderAdapter = new SliderAdapter(this);
        onBoardingSlider.setAdapter(sliderAdapter);

        addDots(0);
        onBoardingSlider.addOnPageChangeListener(changeListener);

        onBoardingSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnBoarding.this, Login.class));
                finish();
            }
        });

        onBoardingNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBoardingSlider.setCurrentItem(currentPosition+1);
            }
        });

        onBoardingGetStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnBoarding.this, Login.class));
                finish();
            }
        });
    }

    private void addDots(int position) {
        dots = new TextView[5];
        onBoardingDots.removeAllViews();

        for(int i=0; i<dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(20);

            onBoardingDots.addView(dots[i]);
        }

        if(dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);

            currentPosition = position;
            if(position == 0) {
                onBoardingSkipButton.setVisibility(View.VISIBLE);
                onBoardingNext.setVisibility(View.VISIBLE);
                onBoardingGetStartedButton.setVisibility(View.INVISIBLE);
            } else if(position == 1) {
                onBoardingSkipButton.setVisibility(View.VISIBLE);
                onBoardingNext.setVisibility(View.VISIBLE);
                onBoardingGetStartedButton.setVisibility(View.INVISIBLE);
            } else if(position == 2) {
                onBoardingSkipButton.setVisibility(View.VISIBLE);
                onBoardingNext.setVisibility(View.VISIBLE);
                onBoardingGetStartedButton.setVisibility(View.INVISIBLE);
            } else if(position == 3) {
                onBoardingSkipButton.setVisibility(View.VISIBLE);
                onBoardingNext.setVisibility(View.VISIBLE);
                onBoardingGetStartedButton.setVisibility(View.INVISIBLE);
            } else {
                onBoardingGetStartedButtonAnimation = AnimationUtils.loadAnimation(OnBoarding.this, R.anim.bottom_animation_onboarding);
                onBoardingGetStartedButton.setAnimation(onBoardingGetStartedButtonAnimation);
                onBoardingGetStartedButton.setVisibility(View.VISIBLE);
                onBoardingSkipButton.setVisibility(View.INVISIBLE);
                onBoardingNext.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}