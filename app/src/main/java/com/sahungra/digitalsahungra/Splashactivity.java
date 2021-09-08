package com.sahungra.digitalsahungra;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.sahungra.digitalsahungra.R;

public class Splashactivity extends AppCompatActivity  {



    Handler h;
    DatabaseReference databaseReference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashactivity);
        databaseReference= FirebaseDatabase.getInstance().getReference("Userdata");
        auth=FirebaseAuth.getInstance();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                h=new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(FirebaseAuth.getInstance().getCurrentUser()!=null && dataSnapshot.hasChild(auth.getCurrentUser().getPhoneNumber()) ){
                            startActivity(new Intent(Splashactivity.this,MainActivity.class));
                            finish();
                        }else{
                            startActivity(new Intent(Splashactivity.this,Introslides.class));
                            finish();
                        }



                    }
                },1000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            }


   }
