package com.sahungra.digitalsahungra;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class Dashboard extends Fragment {

    LinearLayout progressbar;

    ProgressBar male,female,litracy,poppro;

    TextView malet,femalet,litracyt,handicappedt,handicapped,poptext;
    DatabaseReference databaseReference,databaseReference2;
    LinearLayout checkitout;
    CardView flips;

    TextView eventtitle,event_btn_title;
    RoundedImageView event_image;
    String link="";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dashboard, container, false);

        databaseReference=FirebaseDatabase.getInstance().getReference("village_overview");
        databaseReference2= FirebaseDatabase.getInstance().getReference("batch");

        checkitout =v.findViewById(R.id.checkitout);
        AnimationDrawable animationDrawable = (AnimationDrawable) checkitout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        progressbar = v.findViewById(R.id.progressbar);
        flips=v.findViewById(R.id.flips);
        progressbar.setVisibility(View.VISIBLE);
        male=v.findViewById(R.id.malepro);
        eventtitle=v.findViewById(R.id.event_title);
        event_btn_title=v.findViewById(R.id.event_btn_title);
        event_image=v.findViewById(R.id.event_image);
        female=v.findViewById(R.id.femalepro);
        poppro=v.findViewById(R.id.populationpro);
        poptext=v.findViewById(R.id.poptext);
        litracy=v.findViewById(R.id.litracypro);
        handicapped=v.findViewById(R.id.handicappedpro);

        malet=v.findViewById(R.id.maletext);
        femalet=v.findViewById(R.id.femaletext);
        litracyt=v.findViewById(R.id.litracytext);
        handicappedt=v.findViewById(R.id.handicappedtext);

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                batches batch=dataSnapshot.getValue(batches.class);

                if(batch.visibility==true){

                    eventtitle.setText(batch.getTitle());
                    event_btn_title.setText(batch.getButton());
                    Picasso.with(getContext()).load(batch.getImageurl()).into(event_image);
                    link=batch.getUrl();
                    flips.setVisibility(View.VISIBLE);
                }else {
                    flips.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        flips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!link.equals("")){

                    if (!link.startsWith("https://") && !link.startsWith("http://")){
                        link = "http://" + link;
                    }

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(link));
                    startActivity(i);



                }

            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              int population= dataSnapshot.child("population").getValue(Integer.class);
              int m=dataSnapshot.child("male").getValue(Integer.class);
              int f=dataSnapshot.child("female").getValue(Integer.class);
              int l=dataSnapshot.child("litracy").getValue(Integer.class);
              int h=dataSnapshot.child("Handicapped").getValue(Integer.class);


                double a=((double)m/population)*100;

                ObjectAnimator.ofInt(male, "progress",(int) a)
                        .setDuration(900)
                        .start();

                malet.setText("Male ( "+String.format("%.2f", a)+"% )");

                double b=((double)f/population)*100;

                ObjectAnimator.ofInt(female, "progress",(int) b)
                        .setDuration(900)
                        .start();

                femalet.setText("Female ( "+String.format("%.2f",b)+"% )");

                double c=((double)l/population)*100;

                ObjectAnimator.ofInt(litracy, "progress",(int) c)
                        .setDuration(900)
                        .start();

                litracyt.setText("Literancy ( "+String.format("%.2f",c)+"% )");


                handicappedt.setText("Handicapped ");
                handicapped.setText(h+" people");

                double d=((double)population/population)*100;

                ObjectAnimator.ofInt(poppro, "progress",(int) d)
                        .setDuration(900)
                        .start();

                poptext.setText("Population ( " +population+" ) ");







            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        progressbar.setVisibility(View.GONE);


        return v;
    }


}