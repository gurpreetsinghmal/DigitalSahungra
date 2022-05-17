package com.technominds.digitalsahungrav2;

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

public class Assets extends AppCompatActivity {



    List<String> asset_categories;
    asset_adapter asset_adapter;
    DatabaseReference databaseReference;
    RecyclerView asset_rv;
    List<assets_list> assetslist;
    LinearLayout progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets);

        progressbar=findViewById(R.id.progressbar);


        asset_rv=findViewById(R.id.asset_rv);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(Assets.this,RecyclerView.VERTICAL,false);
        asset_rv.setLayoutManager(linearLayoutManager);

        assetslist=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("assets");

        progressbar.setVisibility(View.VISIBLE);



        asset_categories=new ArrayList<>();
        asset_categories.add("Choose Asset Category");
        asset_categories.add("School");
        asset_categories.add("Primary Health Centre");
        asset_categories.add("Religious Places");
        asset_categories.add("Police Stations");
        asset_categories.add("Gym");
        asset_categories.add("NGO");
        asset_categories.add("Library");
        asset_categories.add("Anganwadi");
        asset_categories.add("Playground");
        asset_categories.add("Water Tank");
        asset_categories.add("Others");







        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                assetslist.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    assets_list assets_list=dataSnapshot1.getValue(assets_list.class);
                    assetslist.add(0,assets_list);

                }

                List<String> a=new ArrayList<>();
                a=asset_categories.subList(1,asset_categories.size());


                asset_adapter=new asset_adapter(Assets.this,assetslist,a);
                asset_rv.setAdapter(asset_adapter);
                progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    }






