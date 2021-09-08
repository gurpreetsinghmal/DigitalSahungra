package com.sahungra.digitalsahungra;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class panchayt_members extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    List<member_list> list;
    members_adapter star_adapter;
    LinearLayout progressbar;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panchayat_members);

        list=new ArrayList<>();
        recyclerView=findViewById(R.id.stars_rv);
        progressbar=findViewById(R.id.progressbar);
        progressbar.setVisibility(View.VISIBLE);
        databaseReference= FirebaseDatabase.getInstance().getReference("panchayat_members");
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(panchayt_members.this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    member_list starsList=dataSnapshot1.getValue(member_list.class);
                    list.add(starsList);
                }
                star_adapter=new members_adapter(panchayt_members.this,list);
                recyclerView.setAdapter(star_adapter);
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
