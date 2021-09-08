package com.sahungra.digitalsahungra;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sahungra.digitalsahungra.adapter.updates_adapter;

import java.util.ArrayList;
import java.util.List;


public class recents_updates extends Fragment {
    RecyclerView updates;
    DatabaseReference databaseReference;
    List<update> updatelist;
    updates_adapter adapter;
    LinearLayout progressbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_ud, container, false);


        progressbar=v.findViewById(R.id.progressbar);
        updates=v.findViewById(R.id.postupdates);
        progressbar.setVisibility(View.VISIBLE);


        databaseReference= FirebaseDatabase.getInstance().getReference("updates");

        updatelist=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updatelist.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    update update=dataSnapshot1.getValue(com.sahungra.digitalsahungra.update.class);
                    updatelist.add(update);
                }
                adapter=new updates_adapter(getContext(),updatelist);
                updates.setAdapter(adapter);
                progressbar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        updates.setLayoutManager(linearLayoutManager);



        return v;
    }


}
