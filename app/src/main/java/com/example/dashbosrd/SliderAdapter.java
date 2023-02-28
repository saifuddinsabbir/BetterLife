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

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    int images[] = {
            R.drawable.swipe1,
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

        ImageView slidingImage = view.findViewById(R.id.slidingImageId);
        TextView slideingHeading = view.findViewById(R.id.slideHeadingId);
        TextView slidigDescription = view.findViewById(R.id.slideDescriptionId);

        slidingImage.setImageResource(images[position]);
        slideingHeading.setText(headings[position]);
        slidigDescription.setText(description[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }

}
