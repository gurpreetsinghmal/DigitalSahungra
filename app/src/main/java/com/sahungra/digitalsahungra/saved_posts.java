package com.sahungra.digitalsahungra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sahungra.digitalsahungra.adapter.updates_adapter;

import java.util.ArrayList;
import java.util.List;

public class saved_posts extends AppCompatActivity {

    RecyclerView updates;
    DatabaseReference databaseReference, databaseReference2;
    List<update> updatelist;
    updates_adapter adapter;
    LinearLayout progressbar;
    FirebaseAuth auth;
    LinearLayout listempty;
    List<String> keys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_posts);

        progressbar = findViewById(R.id.progressbar);
        progressbar.setVisibility(View.VISIBLE);
        listempty=findViewById(R.id.listempty);
        auth = FirebaseAuth.getInstance();
        keys = new ArrayList<>();
        updates = findViewById(R.id.savedupdates);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(saved_posts.this, RecyclerView.VERTICAL, false);
        updates.setLayoutManager(linearLayoutManager);
        updatelist = new ArrayList<>();


        Toolbar toolbar = findViewById(R.id.saved_posts_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.blue));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Userdata").child(auth.getCurrentUser().getPhoneNumber()).child("saved");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                keys.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String update = dataSnapshot1.getValue(String.class);
                    keys.add(update);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference2 = FirebaseDatabase.getInstance().getReference("updates");


        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updatelist.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (int i = 0; i < keys.size(); i++) {
                        if (dataSnapshot1.child("key").getValue(String.class).equals(keys.get(i))) {
                            update update = dataSnapshot1.getValue(com.sahungra.digitalsahungra.update.class);
                            updatelist.add(update);

                        }
                    }
                }
                Log.d("mylisu", "onDataChange: "+updatelist.size());
                if(updatelist.size()==0){

                    listempty.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.GONE);
                }else {

                    adapter = new updates_adapter(saved_posts.this, updatelist);
                    updates.setAdapter(adapter);
                    progressbar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
