package com.example.idonor2;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class tabFragment2 extends Fragment implements RecyclerViewAdapter.OnNoteListener{
    FirebaseAuth mAuth;
    String getPhone;
    FirebaseUser user;
    String searchvialoc,searchviablood;
    DatabaseReference databaseReference;
    ArrayAdapter<String> arrayAdapter1,arrayAdapter2;
    Spinner searchbloodtype,searchloc;
    ArrayList<String> districts;
    ArrayList<CardContent> searchResult;
    Button searchUser;
    ProgressBar progressBar;
    String[] BloogGroups={"A+","B+","AB+","O+","O-","AB-","B-","A-"};
    RecyclerView recyclerView;
    public tabFragment2 newInstance() {
        tabFragment2 t2=new tabFragment2();
        return t2;//

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab_fragment2, container, false);
        arrayAdapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,BloogGroups);
        searchbloodtype=view.findViewById(R.id.searchBloodGroup);
        searchloc=view.findViewById(R.id.searchLocation);
        searchUser=view.findViewById(R.id.searchUser);
        searchbloodtype.setAdapter(arrayAdapter1);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        searchResult=new ArrayList<CardContent>();
        if(user!=null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("member");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    CollectDistricts((Map<String, Object>) dataSnapshot.getValue());
                    arrayAdapter2=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,districts);
                    searchloc.setAdapter(arrayAdapter2);
                    searchloc.setSelection(0);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        progressBar=view.findViewById(R.id.progress2);
        searchloc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchvialoc=searchloc.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                searchloc.setSelection(0);
            }
        });
        searchbloodtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchviablood=searchbloodtype.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                searchbloodtype.setSelection(0);
            }
        });
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        clickListnenr();
       
        initializeAdapter();

        return  view;

    }

    private void initializeAdapter() {
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(searchResult,this);
        recyclerView.setAdapter(adapter);


    }

    private void clickListnenr() {
        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                databaseReference.orderByChild("district").equalTo(searchvialoc).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        searchResult.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            if(snapshot.child("userBloodGroup").getValue().toString().equals(searchviablood))
                            {
                                searchResult.add(new CardContent(snapshot.child("userName").getValue().toString(),snapshot.child("userEmail").getValue().toString(),snapshot.child("userPhone").getValue().toString()));
                            }
                        }
                        if(searchResult.isEmpty())
                        {
                            searchResult.add(new CardContent("No Donor Found",null,null));
                        }
                            initializeAdapter();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }


    private void CollectDistricts(Map<String, Object> value) {
        districts=new ArrayList<String>();
        for (Map.Entry<String,Object> entry:value.entrySet())
        {
            Map singleuser=(Map)entry.getValue();
            if(!districts.contains(singleuser.get("district")))
            districts.add((String) singleuser.get("district"));
        }
    }


    @Override
    public void onNoteClick(int adapterPosition, int buttonvalue, String email, String phone) {
        if (buttonvalue==0)
        {}
        if (buttonvalue == 1)
        {
            if(email==null)
            {
                Toast.makeText(getContext(),"No Donor Found",Toast.LENGTH_LONG).show();
            }
            else
            sendEmail(email);
        }
        if (buttonvalue==2)
        {
            getPhone=phone;
            if(phone==null)
            {
                Toast.makeText(getContext(),"No Donor Found",Toast.LENGTH_LONG).show();
            }
            else
            callPhoneNumber(phone);

        }
        if (buttonvalue==3)
        {
            if(phone==null)
            {
                Toast.makeText(getContext(),"No Donor Found",Toast.LENGTH_LONG).show();
            }
            else
            sendSms(phone);
        }
    }
    private void callPhoneNumber(String phone) {
        try{if(Build.VERSION.SDK_INT>22){
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},101);
                return;
            }
            Intent callIntent=new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phone));
            startActivity(callIntent);


        }
        else
        {
            Intent callIntent=new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phone));
            startActivity(callIntent);
        }}catch (Exception e){e.printStackTrace();}

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==101)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                callPhoneNumber(getPhone);
            }
        }
       else if(requestCode==100)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                sendSms(getPhone);
            }
        }
    }

    private void sendEmail(String email) {
        Log.i("Send email", "");

        String subject="Donor Needed Please Contact";
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto",email,null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please help Donor Required");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Toast.makeText(getActivity(), "There is  email client installed.", Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendSms(String phone) {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.SEND_SMS},
                    100);


        } else
        {
            String messagetosend="Donor Needed Please Contact";
            SmsManager.getDefault().sendTextMessage(phone,null,messagetosend,null,null);
            Toast.makeText(getContext(),"Message Sent",Toast.LENGTH_LONG).show();
        }


    }
}
