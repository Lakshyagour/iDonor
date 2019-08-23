package com.example.idonor2;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;
    DatabaseReference databaseReference,reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        if (user!=null){
            uid =user.getUid();
            databaseReference= FirebaseDatabase.getInstance().getReference().child("member");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getKey().equals(uid))
                    {Toast.makeText(getApplicationContext(),"In this loop",Toast.LENGTH_LONG).show();

                    }
                    else
                        CollectAndAdd((Map<String, Object>) dataSnapshot.getValue());


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(),"Click On Marker To get Donor Information",Toast.LENGTH_LONG).show();
    }

    private void CollectAndAdd(Map<String, Object> value) {
        for (Map.Entry<String,Object> entry:value.entrySet())
        { Map singleuser=(Map)entry.getValue();
            if(entry.getKey().equals(uid))
            {
                String[] latLng=singleuser.get("userLatLng").toString().split(",");
                Double lat= Double.parseDouble(latLng[0]);
                Double lng= Double.parseDouble(latLng[1]);
                LatLng location=new LatLng(lat,lng);
                mMap.addMarker(new MarkerOptions().position(location).title("My Location").icon(bitmapDescriptor(this,R.drawable.ic_person_pin_circle_black_24dp)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, (float) 12));
            }
            else
            {

            String[] latLng=singleuser.get("userLatLng").toString().split(",");
            Double lat= Double.parseDouble(latLng[0]);
            Double lng= Double.parseDouble(latLng[1]);
            LatLng location=new LatLng(lat,lng);
            mMap.addMarker(new MarkerOptions().position(location).title(singleuser.get("userName").toString()+","+singleuser.get("userBloodGroup")));
        }}
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    private BitmapDescriptor bitmapDescriptor(Context context,int vectorDrawableRecource)
    {
        Drawable vectordrawable= ContextCompat.getDrawable(context,vectorDrawableRecource);
        vectordrawable.setBounds(0,0,vectordrawable.getIntrinsicWidth(),vectordrawable.getIntrinsicHeight());
        Bitmap bitmap=Bitmap.createBitmap(vectordrawable.getIntrinsicWidth(), vectordrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        vectordrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
