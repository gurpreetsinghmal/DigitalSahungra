package com.sahungra.digitalsahungra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class panchayt_member_profile_editor extends AppCompatActivity {
    TextView name,education,currently_doing,hobbies,business,dob,phn;

    String key;

    DatabaseReference databaseReference;
    LinearLayout editname,editbio,editdate,editphone;
    LinearLayout progressbar;
    RoundedImageView imageView;
    ImageButton chooser;
    String url=null;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panchayt_member_profile_editor);
        databaseReference= FirebaseDatabase.getInstance().getReference("panchayat_members");


        toolbar=findViewById(R.id.toolbar_profile_editor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        chooser=findViewById(R.id.chooser);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.color2));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        imageView=findViewById(R.id.user_profile_pic);
        name=findViewById(R.id.membername);
        editbio=findViewById(R.id.editbio);
        editdate=findViewById(R.id.editdob);
        editphone=findViewById(R.id.editphn);
        education=findViewById(R.id.education);
        currently_doing=findViewById(R.id.currently_doing);
        hobbies=findViewById(R.id.hobbies);
        editname=findViewById(R.id.editname);
        business=findViewById(R.id.business);
        progressbar=findViewById(R.id.progressbar);
        dob=findViewById(R.id.memberdob);
        phn=findViewById(R.id.memberphn);
        key=getIntent().getStringExtra("key");

        progressbar.setVisibility(View.VISIBLE);
        databaseReference= FirebaseDatabase.getInstance().getReference("panchayat_members").child(key);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                member_list member_list=dataSnapshot.getValue(member_list.class);
                name.setText(member_list.getName());
                education.setText(member_list.getEducation());
                currently_doing.setText(member_list.getCurrently_doing());
                hobbies.setText(member_list.getHobbies());
                dob.setText(member_list.getDob());
                phn.setText(member_list.getPhone());

                business.setText(member_list.getBusiness());
                url=member_list.getUrl();
                Picasso.with(panchayt_member_profile_editor.this).load(member_list.getUrl()).into(imageView);
                progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent i=new Intent(panchayt_member_profile_editor.this,zoom_image.class);
               i.putExtra("url",url);
               startActivity(i);
            }
        });











    }







    }

