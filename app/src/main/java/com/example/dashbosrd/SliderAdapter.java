package com.example.dashbosrd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    int images[] = {
            R.raw.ambulance,
            R.drawable.swip2,
            R.drawable.swip5,
            R.drawable.three,
            R.drawable.one
    };

    int headings[] = {
            R.string.first_heading,
            R.string.second_heading,
            R.string.third_heading,
            R.string.fourth_heading,
            R.string.fifth_heading
    };

    int description[] = {
            R.string.first_desc,
            R.string.second_desc,
            R.string.third_desc,
            R.string.fourth_desc,
            R.string.fifth_desc
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slides_layout, container, false);

        LottieAnimationView slidingImage1 = view.findViewById(R.id.slidingImageId);
        LottieAnimationView slidingImage2 = view.findViewById(R.id.slidingImageId2);
        LottieAnimationView slidingImage3 = view.findViewById(R.id.slidingImageId3);
        LottieAnimationView slidingImage4 = view.findViewById(R.id.slidingImageId4);
        LottieAnimationView slidingImage5 = view.findViewById(R.id.slidingImageId5);

        TextView slideingHeading = view.findViewById(R.id.slideHeadingId);
        TextView slidigDescription = view.findViewById(R.id.slideDescriptionId);

        if(position == 0) {
            slidingImage1.setVisibility(View.VISIBLE);
            slidingImage2.setVisibility(View.INVISIBLE);
            slidingImage3.setVisibility(View.INVISIBLE);
            slidingImage4.setVisibility(View.INVISIBLE);
            slidingImage5.setVisibility(View.INVISIBLE);
        } else if(position == 1) {
            slidingImage1.setVisibility(View.INVISIBLE);
            slidingImage2.setVisibility(View.VISIBLE);
            slidingImage3.setVisibility(View.INVISIBLE);
            slidingImage4.setVisibility(View.INVISIBLE);
            slidingImage5.setVisibility(View.INVISIBLE);
        } else if(position == 2) {
            slidingImage1.setVisibility(View.INVISIBLE);
            slidingImage2.setVisibility(View.INVISIBLE);
            slidingImage3.setVisibility(View.VISIBLE);
            slidingImage4.setVisibility(View.INVISIBLE);
            slidingImage5.setVisibility(View.INVISIBLE);
        } else if(position == 3) {
            slidingImage1.setVisibility(View.INVISIBLE);
            slidingImage2.setVisibility(View.INVISIBLE);
            slidingImage3.setVisibility(View.INVISIBLE);
            slidingImage4.setVisibility(View.VISIBLE);
            slidingImage5.setVisibility(View.INVISIBLE);
        } else {
            slidingImage1.setVisibility(View.INVISIBLE);
            slidingImage2.setVisibility(View.INVISIBLE);
            slidingImage3.setVisibility(View.INVISIBLE);
            slidingImage4.setVisibility(View.INVISIBLE);
            slidingImage5.setVisibility(View.VISIBLE);
        }
        slideingHeading.setText(headings[position]);
//        slidigDescription.setText(description[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }

}
