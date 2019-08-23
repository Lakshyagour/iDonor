package com.example.idonor2;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class tabFragment4 extends Fragment {
DatabaseClass databaseClass;
Context context;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference reff;
    Button editProfile;
    TextView nametext,emailtext,phonetext,bloodtext,gendertext;
    String name,email,phone,bloodgroup,gender;
    public static String myPreferences="myLoc";
    public static tabFragment4 newInstances() {
        tabFragment4 t4=new tabFragment4();
        return t4;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.tab_fragment4, container, false);
        mAuth= FirebaseAuth.getInstance();
        databaseClass=new DatabaseClass(getContext());
        user=FirebaseAuth.getInstance().getCurrentUser();
        String uid = null;
        nametext=view.findViewById(R.id.viewuserName);
        emailtext=view.findViewById(R.id.viewemailId);
        bloodtext=view.findViewById(R.id.viewbloodGroup);
        phonetext=view.findViewById(R.id.viewmobileNo);
        gendertext=view.findViewById(R.id.viewgender);
        editProfile=view.findViewById(R.id.editProfile);
        CircleImageView circleImageView=view.findViewById(R.id.viewprofilePhoto);

        final ProgressBar progressBar=view.findViewById(R.id.progress4);
        if(user!=null)
        { uid=user.getUid();

        progressBar.setVisibility(View.VISIBLE);
            reff= FirebaseDatabase.getInstance().getReference().child("member").child(uid);
            reff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    emailtext.setText(dataSnapshot.child("userEmail").getValue().toString());
                    nametext.setText(dataSnapshot.child("userName").getValue().toString());
                    bloodtext.setText(dataSnapshot.child("userBloodGroup").getValue().toString());
                    gendertext.setText(dataSnapshot.child("userGender").getValue().toString());
                   phonetext.setText(dataSnapshot.child("userPhone").getValue().toString());
                   progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Main6Activity.class));
            }
        });

        return  view;

    }

}
