package com.example.idonor2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class Main2Activity extends AppCompatActivity  {
    private static final int RC_SIGN_IN =101 ;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseAuth.AuthStateListener authStateListener;
    Button login;
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        mAuth=FirebaseAuth.getInstance();
        login=findViewById(R.id.loginBtn);
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=mAuth.getCurrentUser();
                if(user==null)
                {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .setLogo(R.mipmap.ic_launcher_foreground)
                                    .build(),
                            RC_SIGN_IN);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Successfull Login In",Toast.LENGTH_LONG).show();
                    updateUI(user);

                }
            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mAuth.addAuthStateListener(authStateListener);
            }
        });

    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                updateUI(user);
                //
            } else {
                Toast.makeText(getApplicationContext(),"Authentication Failed",Toast.LENGTH_LONG).show();

            }
        }
    }

    private void updateUI(FirebaseUser user) {
        if(user!=null){
            progressBar=findViewById(R.id.indefinite);
            progressBar.setVisibility(View.VISIBLE);
            login.setEnabled(false);

            String uid=user.getUid();
            DatabaseReference reff= FirebaseDatabase.getInstance().getReference().child("member").child(uid);
            reff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        startActivity(new Intent(Main2Activity.this,Main4Activity.class));
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();

                    }
                    else
                    {
                        startActivity(new Intent(Main2Activity.this,Main6Activity.class));
                        finish();
                    }
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
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null){
            progressBar=findViewById(R.id.indefinite);
            progressBar.setVisibility(View.VISIBLE);
            login.setEnabled(false);

            String uid=user.getUid();
            DatabaseReference reff= FirebaseDatabase.getInstance().getReference().child("member").child(uid);
            reff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        startActivity(new Intent(Main2Activity.this,Main4Activity.class));
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();

                    }
                    else
                    {
                        startActivity(new Intent(Main2Activity.this,Main6Activity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
        {
            mAuth.addAuthStateListener(authStateListener);
        }

    }
}
