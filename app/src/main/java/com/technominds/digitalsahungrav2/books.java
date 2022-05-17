package com.technominds.digitalsahungrav2;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class books extends Fragment {

    RecyclerView recyclerView;
    official_forms_adapter official_forms_adapter;
    List<official_form_list> list;
    DatabaseReference databaseReference;
    LinearLayout progressbar;
    StorageReference storageReference;
    EditText search;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_forms, container, false);
        databaseReference= FirebaseDatabase.getInstance().getReference("books");
        storageReference= FirebaseStorage.getInstance().getReference("books/");
        recyclerView=v.findViewById(R.id.forms_rv);
        progressbar=v.findViewById(R.id.progressbar);
        search=v.findViewById(R.id.formsearch);


        list=new ArrayList<>();

        progressbar.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    official_form_list listitem=dataSnapshot1.getValue(official_form_list.class);
                    list.add(listitem);
                }

                official_forms_adapter=new official_forms_adapter(getContext(),list);
                recyclerView.setAdapter(official_forms_adapter);

                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                official_forms_adapter=new official_forms_adapter(getContext(),list);

                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        return v;
    }







    private  void filter(String text){
        List<official_form_list> filterlist=new ArrayList<>();
        for(official_form_list item:list){
            if(item.getName().toLowerCase().contains(text.toLowerCase())||item.getDescription().toLowerCase().contains(text.toLowerCase())){
                filterlist.add(item);
            }
        }
        official_forms_adapter.filterlist(filterlist);

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(official_forms_adapter);


    }

}
