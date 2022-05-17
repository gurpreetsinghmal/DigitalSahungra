package com.technominds.digitalsahungrav2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
