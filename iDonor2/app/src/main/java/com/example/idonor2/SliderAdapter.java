package com.example.idonor2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import de.hdodenhof.circleimageview.CircleImageView;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public SliderAdapter(Context context)
    {
        this.context=context;
    }
    public int[] slideImages={
            R.drawable.a,
            R.drawable.b,
            R.drawable.c

    };
    public String[] iconTitle={
            "Find Blood\nDonors",
            "Donate Blood",
            "Save Lives"
    };


    @Override
    public int getCount() {
        return iconTitle.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==(RelativeLayout)o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view= (View) layoutInflater.inflate(R.layout.sliding_layout,container,false);
        CircleImageView imageView=view.findViewById(R.id.iconI);
        TextView textView=view.findViewById(R.id.iconTitle);

        imageView.setImageResource(slideImages[position]);
        textView.setText(iconTitle[position]);
        container.addView(view);

        return  view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
