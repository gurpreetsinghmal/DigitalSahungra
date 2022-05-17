package com.technominds.digitalsahungrav2;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technominds.digitalsahungrav2.adapter.updates_adapter;

import java.util.ArrayList;
import java.util.Collections;
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
                    update update=dataSnapshot1.getValue(com.technominds.digitalsahungrav2.update.class);
                    updatelist.add(update);
                }
                Collections.reverse(updatelist);
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
