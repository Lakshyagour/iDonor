package com.example.idonor2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main6Activity extends AppCompatActivity {
    DatabaseClass databaseClass;
    DatabaseReference reff;
    SharedPreferences sharedPreferences;
    Member member;
    Bitmap imgBitmap;
    String uid;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String firbaseusername;
    String firebaseuseremail;
    public static String myPreferences="myLoc";
    public static String myCountry="myCountry";
    public static String myState="myState";
    public static String myDistrict="myDistrict";
    public static String myCity="myCity";
    public static String myPin="myPin";
    public static String myLatLng="myLatLng";
    String country,state,district,city,pin;
    String userName,userEmail,userPhone,userBloodGroup,userGender,userDOB,userLatLng;
    private int resultCode=101;
    EditText name,email,mobileno;
    TextView textView;
    Context context;
    Spinner bloodGroup;
    RadioGroup group;
    String[] BloogGroups={"A+","B+","AB+","O+","O-","AB-","B-","A-"};
    RadioButton male,female;
    Button getLocation,createProfile,dob;
    RadioButton radioButton;
    CircleImageView userPhoto;
    View view;
    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar;
    private  static final int PickImage=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        databaseClass=new DatabaseClass(getApplicationContext());
        getIds();
        clickListener();
        imgBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.ic_person_black_242dp);
        myCalendar = Calendar.getInstance();
        date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();

            }
        };
        mAuth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {


            for (UserInfo userInfo : user.getProviderData()) {
                if (userInfo.getEmail() != null)
                {
                    firebaseuseremail = userInfo.getEmail();
                }
                if (userInfo.getDisplayName() != null)
                {
                    firbaseusername = userInfo.getDisplayName();
                }
            }
            boolean emailVerified = user.isEmailVerified();
            uid = user.getUid();
            reff= FirebaseDatabase.getInstance().getReference().child("member").child(uid);
            reff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        email.setText(dataSnapshot.child("userEmail").getValue().toString());
                        name.setText(dataSnapshot.child("userName").getValue().toString());
                        bloodGroup.setSelection(getIndex(bloodGroup, dataSnapshot.child("userBloodGroup").getValue().toString()));
                        dob.setText(dataSnapshot.child("userDOB").getValue().toString());
                        mobileno.setText(dataSnapshot.child("userPhone").getValue().toString());
                        group.check(getGenderIndex(dataSnapshot.child("userGender").getValue().toString()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }
        email.setText(firebaseuseremail);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(Main6Activity.this,android.R.layout.simple_list_item_1,BloogGroups);
        bloodGroup.setAdapter(arrayAdapter);
        member=new Member();
        reff= FirebaseDatabase.getInstance().getReference().child("member");
        sharedPreferences=getSharedPreferences(myPreferences,Context.MODE_PRIVATE);

    }

    private int getGenderIndex(String userGender) {
        if(userGender.equalsIgnoreCase("Male"))
        {
            return R.id.gMale;
        }
        else if(userGender.equalsIgnoreCase("Female"))
            return R.id.gFemale;

        return R.id.gMale;
    }


    private int getIndex(Spinner bloodGroup, String userBloodGroup) {
        for(int i=0;i<bloodGroup.getCount();i++)
        {
            if(bloodGroup.getItemAtPosition(i).toString().equalsIgnoreCase(userBloodGroup))
                return i;
        }
        return 0;
    }

    private void getIds() {
        name=findViewById(R.id.userName);
        email=findViewById(R.id.emailId);
        mobileno=findViewById(R.id.mobileNo);
        bloodGroup=findViewById(R.id.bloodGroup);
        group=findViewById(R.id.gender);
        male=findViewById(R.id.gMale);
        female=findViewById(R.id.gFemale);
        getLocation=findViewById(R.id.LOC);
        dob=findViewById(R.id.DOB);
        userPhoto=findViewById(R.id.userprofilePhoto);
        createProfile=findViewById(R.id.createProfile);
        textView=findViewById(R.id.fullInfo);
        group.check(R.id.gMale);
    }
    private void clickListener() {

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Main6Activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery=new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"select Picture"),PickImage);
            }
        });
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Main6Activity.this,Main5Activity.class),resultCode);
            }
        });
        createProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformation();
                if(userName.isEmpty() || userPhone.isEmpty() || userEmail.isEmpty() || userBloodGroup.isEmpty() || userDOB.isEmpty() || userGender.isEmpty())
                {
                    if(userName.length()==0)
                    {
                        name.setError("Empty Field");
                    }
                    if(userEmail.length()==0)
                    {
                        email.setError("Empty Field");
                    }
                    if(userPhone.length()==0)
                    {
                        mobileno.setError("Empty Field");
                    }

                    if(userGender.length()==0)
                    {
                        group.check(R.id.gMale);
                    }
                    if(userDOB.equals("Click Here!!"))
                    {
                        dob.setError("Select Date");
                    }

                }
                else
                {
                    member.setUserLatLng(userLatLng);
                    member.setUserName(userName);
                    member.setUserEmail(userEmail);
                    member.setUserPhone(userPhone);
                    member.setUserBloodGroup(userBloodGroup);
                    member.setUserDOB(userDOB);
                    member.setUserGender(userGender);
                    member.setCity(city);
                    member.setCountry(country);
                    member.setDistrict(district);
                    member.setState(state);
                    member.setPin(pin);
                    reff.child(uid).setValue(member);
                    startActivity(new Intent(Main6Activity.this,Main4Activity.class));}
            }
        });


    }
    private void getInformation() {
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
        if(sharedPreferences.contains(myLatLng))
            userLatLng=sharedPreferences.getString(myLatLng,"");
        else
            userLatLng="0,0";

        userName=name.getText().toString();
        userEmail=email.getText().toString();
        userPhone=mobileno.getText().toString();
        userDOB=dob.getText().toString();
        userBloodGroup=bloodGroup.getSelectedItem().toString();
        radioButton=findViewById(group.getCheckedRadioButtonId());
        userGender=radioButton.getText().toString();

    }
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dob.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PickImage && resultCode==RESULT_OK)
        {
            Uri imageUri = data.getData();
            try {
                imgBitmap= MediaStore.Images.Media.getBitmap(Main6Activity.this.getContentResolver(),imageUri);
                userPhoto.setImageBitmap(imgBitmap);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private byte[] getBitmapAsArray(Bitmap img) {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
        return  outputStream.toByteArray();
    }
}
