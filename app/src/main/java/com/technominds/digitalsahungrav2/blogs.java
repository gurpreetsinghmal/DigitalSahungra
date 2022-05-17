package com.technominds.digitalsahungrav2;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

import java.util.ArrayList;
import java.util.List;


public class blogs extends Fragment {

    CardView compose;
    RecyclerView recyclerView;
    List<blog_envelope> list;

    DatabaseReference databaseReference;
    blog_Adapter adapter;
    LinearLayout progressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


       View v= inflater.inflate(R.layout.fragment_blogs, container, false);
       recyclerView=v.findViewById(R.id.bloglime);
       progressbar=v.findViewById(R.id.progressbar);
       final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
       recyclerView.setLayoutManager(linearLayoutManager);
       list=new ArrayList<>();
       progressbar.setVisibility(View.VISIBLE);

     compose=v.findViewById(R.id.compose_blog);
     databaseReference= FirebaseDatabase.getInstance().getReference("blogs");

     databaseReference.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             list.clear();
             for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                 blog_envelope blog=dataSnapshot1.getValue(blog_envelope.class);
                 list.add(0,blog);

             }
             adapter=new blog_Adapter(getContext(),list);
             recyclerView.setAdapter(adapter);
             progressbar.setVisibility(View.GONE);



         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });




     compose.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent i=new Intent(getContext(),blog_composer.class);
             startActivity(i);

         }
     });


       return v;
    }

}
