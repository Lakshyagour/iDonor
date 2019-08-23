package com.example.idonor2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main4Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String myPreferences="myLoc";
    public static String myCountry="myCountry";
    public static String myState="myState";
    public static String myDistrict="myDistrict";
    public static String myCity="myCity";
    public static String myPin="myPin";
    public static String myLatLng="myLatLng";
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;
    LinearLayout linearLayoutll;
    DatabaseReference reff;
    DatabaseReference reffchild;
    FirebaseUser user;
    FloatingActionButton fab;
    String email,name;
    TabLayout tbl_pages;
    NavigationView navigationView;
    public TextView textemail,textname;
    CircleImageView circleImageView;
    Uri photoUrl;
    String country,state,district,city,pin;
    Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        mAuth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
toolbar=findViewById(R.id.toolbar);

        linearLayoutll=findViewById(R.id.navHeader);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String uid = null;
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (user != null) {
            // Name, email address, and profile photo Url

            for (UserInfo userInfo : user.getProviderData()) {
                if (userInfo.getEmail() != null)
                {
                    email = userInfo.getEmail();
                }
                if (userInfo.getDisplayName() != null)
                {
                    name = userInfo.getDisplayName();
                }
                if (userInfo.getPhotoUrl() != null)
                {
                    photoUrl=userInfo.getPhotoUrl();
                }


            }
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
            uid = user.getUid();

        }else
        {
            textemail.setText("iDonor@gmail.com");
        }

        reff= FirebaseDatabase.getInstance().getReference().child("member");
reffchild=reff.child(uid);
        sharedPreferences=getSharedPreferences(myPreferences, Context.MODE_PRIVATE);

fillActivity();
addemailInfoOnHeader();
    }

    private void addemailInfoOnHeader() {
        //add name and email to navbar header

        View header=navigationView.getHeaderView(0);
        textemail=header.findViewById(R.id.textmail);
        textemail.setText(email);
        textname=header.findViewById(R.id.textname);
        textname.setText(name);
        circleImageView=header.findViewById(R.id.userImage);
    }


    private void fillActivity() {
        ViewPager vp_pages= findViewById(R.id.vp_pages);
        vp_pages.setOffscreenPageLimit(2);
        PagerAdapter pagerAdapter=new FragmentAdapter(getSupportFragmentManager());
        vp_pages.setAdapter(pagerAdapter);

         tbl_pages=  findViewById(R.id.tbl_pages);
        tbl_pages.setupWithViewPager(vp_pages);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main4, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.lightMode) {


        }
        if (id == R.id.darkMode) {


        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_myLocation) {
            if(sharedPreferences.contains(myCountry))
                country=sharedPreferences.getString(myCountry,"");
            else
                country="Unidentified";
            if(sharedPreferences.contains(myState))
                state=sharedPreferences.getString(myState,"");
            else
                state="Unidentified";
            if(sharedPreferences.contains(myDistrict))
                district=sharedPreferences.getString(myDistrict,"");
            else
                district="Unidentified";
            if(sharedPreferences.contains(myCity))
                city=sharedPreferences.getString(myCity,"");
            else
                city="Unidentified";
            if(sharedPreferences.contains(myPin))
                pin=sharedPreferences.getString(myPin,"");
            else
                pin="Unidentified";

            Toast.makeText(getApplicationContext(),"Your Current Location is saved as \n"+city,Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_DonorMap) {
            startActivity(new Intent(Main4Activity.this,MapsActivity.class));

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        else if (id == R.id.nav_logOut) {
            signOut();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void signOut()
    {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Successfully Signed Out",Toast.LENGTH_LONG).show();


                        startActivity(new Intent(Main4Activity.this,Main2Activity.class));
                        finish();

                    }
                });
    }

    public class FragmentAdapter extends FragmentPagerAdapter {
        private final FragmentManager mFragmentManager;
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager=fm;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:

                    return tabFragment4.newInstances();

                case 1:

                    return new tabFragment2().newInstance();
                case 2:

                    return new tabFragment3().newInstance();

                 default:
                     return null;
            }

        }

        @Override
        public int getCount() {
            return 3;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                //
                //Your tab titles
                //
                case 0:return "Profile";
                case 1:return "Search";
                case 2: return "Contact";
                default:return null;
            }
        }


    }

    public interface FirstPageFragmentListener
    {
        void onSwitchToNextFragment();
    }
}
