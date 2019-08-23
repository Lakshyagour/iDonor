package com.example.idonor2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Main5Activity extends AppCompatActivity implements LocationListener {

    EditText usercountry,userstate,usercity,userpin,userDistrict;
    SharedPreferences sharedPreferences;
    public static String myPreferences="myLoc";
    public static String myCountry="myCountry";
    public static String myState="myState";
    public static String myDistrict="myDistrict";
    public static String myCity="myCity";
    public static String myPin="myPin";
    public static String myLatLng="myLatLng";

String mylatlng;

    ProgressBar progressBar;
    TextToSpeech tts;
    Button refresh,confirm;
    LocationManager locationManager;
    LatLng latLng;
    String country,state,district,city,pin;
    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        getIds();
        progressBarClick();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else {
            requestLocation();
        }
        if (!isLocationEnabled()) {
            showAlert(1);
        }
        userpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        clickListener();
        sharedPreferences=getSharedPreferences(myPreferences, Context.MODE_PRIVATE);



    }

    private void getIds() {
        usercountry=findViewById(R.id.userCountry);
        userstate=findViewById(R.id.userState);
        usercity=findViewById(R.id.userCity);
        userpin=findViewById(R.id.userPin);
        userDistrict=findViewById(R.id.userDistrict);
        progressBar=findViewById(R.id.progressbar);
        confirm=findViewById(R.id.confirm);
        refresh=findViewById(R.id.refresh);
    }

    private void clickListener() {
        refresh.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                usercity.setText("");
                userDistrict.setText("");
                usercountry.setText("");
                userpin.setText("");
                userstate.setText("");
                requestLocation();
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(myCountry,country);
                editor.putString(myState,state);
                editor.putString(myDistrict,district);
                editor.putString(myCity,city);
                editor.putString(myPin,pin);
                editor.putString(myLatLng,mylatlng);
                editor.commit();
                if(userpin.getText().toString().isEmpty() || usercountry.getText().toString().isEmpty() || userstate.getText().toString().isEmpty() || usercity.getText().toString().isEmpty() || userDistrict.getText().toString().isEmpty())
                {
                    usercity.setText("");
                    userDistrict.setText("");
                    usercountry.setText("");
                    userpin.setText("");
                    userstate.setText("");
                    requestLocation();
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Incorrect Location Try Refreshing \n Please Wait!!",Toast.LENGTH_LONG).show();
                }
                else {
                finish();}
            }
        });
    }
//as name suggests show alert dailog box
    private void showAlert(final int i) {
        String message = null, title = null, btnText = null;
        if (i == 1) {
            message = "Donor Location Needed For Creating Account";
            title = "Kindly Enable location";
            btnText = "Grant";

        }

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnText, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (i == 1) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }

                        else {
                            requestPermissions(PERMISSIONS, PERMISSION_ALL);

                        }

                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.show();
    }
    //this method requests location
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        String provider = locationManager.getBestProvider(criteria, true);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        locationManager.requestLocationUpdates(provider, 0, 0, this);



    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isPermissionGranted() {

        if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) || (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        geoCoding(location);
        if(!(userpin.getText().toString().isEmpty() || usercountry.getText().toString().isEmpty() || userstate.getText().toString().isEmpty() || usercity.getText().toString().isEmpty() || userDistrict.getText().toString().isEmpty()))
        {

            locationManager.removeUpdates(this);

        }

    }
    //this method gets current location to textfeilds
    void geoCoding(Location loc)
    {
        Geocoder geocoder= new Geocoder(this,
                Locale.getDefault());

        if(geocoder!=null)
        {
            List<Address> addresses= null;
            try {
                addresses = geocoder.getFromLocation(
                        loc.getLatitude(),
                        loc.getLongitude(),
                        1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            latLng=new LatLng(loc.getLatitude(),loc.getLongitude());
            Address address = addresses.get(0);

            StringBuilder sb = new StringBuilder();

            for(int i=0; i<=address.getMaxAddressLineIndex(); i++)
            {
                sb.append(address.getAddressLine(i));
            }
            state = address.getAdminArea();

            city = address.getLocality();

            country = address.getCountryName();

            pin = address.getPostalCode();

            district = address.getSubAdminArea();

            usercountry.setText(country);
            usercity.setText(city);
            userstate.setText(state);
            userpin.setText(pin);
            userDistrict.setText(district);

            mylatlng=loc.getLatitude()+","+loc.getLongitude();



        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(getApplicationContext(),"Status changed",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getApplicationContext(),"Provider Enabled",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(),"Provider disabled",Toast.LENGTH_LONG).show();

    }
    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }
    public  void progressBarClick()
    {
        progressBar.setVisibility(View.VISIBLE);
    }



}
