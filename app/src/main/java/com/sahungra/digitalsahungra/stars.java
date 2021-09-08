package com.sahungra.digitalsahungra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class stars extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    List<stars_list> list;
    star_adapter star_adapter;
    LinearLayout progressbar;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stars);

        list=new ArrayList<>();
        recyclerView=findViewById(R.id.stars_rv);
        progressbar=findViewById(R.id.progressbar);
        progressbar.setVisibility(View.VISIBLE);
        databaseReference= FirebaseDatabase.getInstance().getReference("stars");
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(stars.this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    stars_list starsList=dataSnapshot1.getValue(stars_list.class);
                    list.add(starsList);
                }
                star_adapter=new star_adapter(stars.this,list);
                recyclerView.setAdapter(star_adapter);
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
