package com.example.idonor2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ViewPager mslideViewPager;
    private LinearLayout mDotLayout;
    SliderAdapter sliderAdapter;
    TextView[] mdots;
    Context context;
    int currentPage;
    Button next,back;
    FirebaseAuth mAuth;
    DatabaseReference reff;
    DatabaseReference reffchild;


    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mslideViewPager=findViewById(R.id.viewPager);
        mDotLayout=findViewById(R.id.mDotLayout);
        sliderAdapter=new SliderAdapter(this);
        mslideViewPager.setAdapter(sliderAdapter);
        back=findViewById(R.id.back);
        next=findViewById(R.id.next);
        mAuth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        addDots(0);
        mslideViewPager.addOnPageChangeListener(changeListener);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mslideViewPager.setCurrentItem(currentPage-1);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage==0 || currentPage==1) {
                    mslideViewPager.setCurrentItem(currentPage + 1);
                }
                else if(currentPage==2)
                {

                    Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });
    }
    public  void addDots(int position)
    {
        mdots=new TextView[3];
        mDotLayout.removeAllViews();
        for(int i=0;i<3;i++)
        {
            mdots[i]=new TextView(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mdots[i].setText(Html.fromHtml("&#8226",Html.FROM_HTML_MODE_LEGACY));
            }
            mdots[i].setTextSize(35);
            mdots[i].setTextColor(getResources().getColor(R.color.dotColor));
            mDotLayout.addView(mdots[i]);
        }
        if(mdots.length>0)
        {
            mdots[position].setTextColor(getResources().getColor(R.color.colorAccent));
        }

    }


    ViewPager.OnPageChangeListener changeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {


        }

        @Override
        public void onPageSelected(int i) {
            addDots(i);
            currentPage=i;
            if(i==0)
            {
                back.setEnabled(false);
                next.setEnabled(true);
                next.setText("Next");
                back.setVisibility(View.INVISIBLE);
                back.setText("");
            }
            else if(i==1)
            {
                back.setEnabled(true);
                next.setEnabled(true);
                next.setText("Next");
                back.setVisibility(View.VISIBLE);
                back.setText("Back");
            }
            else if(i==2)
            {
                back.setEnabled(true);
                next.setEnabled(true);
                next.setText("Finish");
                back.setVisibility(View.VISIBLE);
                back.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){
            Intent intent=new Intent(MainActivity.this,Main2Activity.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();
        }
    }

}
