package com.sahungra.digitalsahungra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class blog_composer extends AppCompatActivity {



    Toolbar toolbar;
    List<blog_list> bloglist;
    blog_list blogspan;
    RecyclerView recyclerView;
    LinearLayout progressbar;
    new_blog_timeline new_blog;
    private static final int PICK_IMAGE_REQUEST = 123;
    String key;
    Uri resulturi;

    StorageReference storageReference;
    Uri filePath;
    LinearLayout heading,subtitle,para,blogpic,hyperlink;
    DatabaseReference databaseReference,databaseReference2;
    FirebaseAuth auth;
    String name,url;
    LinearLayout emptyblog;


    Button publish,discard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_composer);
        auth=FirebaseAuth.getInstance();
        discard=findViewById(R.id.discard);
        publish=findViewById(R.id.publish);


        heading=findViewById(R.id.heading);
        subtitle=findViewById(R.id.subtitle);
        para=findViewById(R.id.para);
        blogpic=findViewById(R.id.blogpic);
        hyperlink=findViewById(R.id.hyperlink);
        emptyblog=findViewById(R.id.emptyblog);

        databaseReference2=FirebaseDatabase.getInstance().getReference("Userdata").child(auth.getCurrentUser().getPhoneNumber());



        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name=dataSnapshot.child("name").getValue(String.class);
                url=dataSnapshot.child("url").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference= FirebaseDatabase.getInstance().getReference("blogs");

        key=databaseReference.push().getKey();
        storageReference = FirebaseStorage.getInstance().getReference("blogs/"+auth.getCurrentUser().getPhoneNumber()+"/"+key+"/");

        recyclerView=findViewById(R.id.blog_time_line);

        progressbar=findViewById(R.id.progressbar);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(blog_composer.this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        bloglist=new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("New Blog");

        new_blog=new new_blog_timeline(blog_composer.this,bloglist);
        recyclerView.setAdapter(new_blog);

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(bloglist.size()==0){
                    Toast.makeText(blog_composer.this, "Empty blog can't be published", Toast.LENGTH_SHORT).show();
                }else{
                    Date date= Calendar.getInstance().getTime();

                    blog_envelope blog_cover=new blog_envelope(name,url,date,key,bloglist,auth.getCurrentUser().getPhoneNumber());
                    databaseReference.child(key).setValue(blog_cover);
                Toast.makeText(blog_composer.this, "Blog published Successfully !", Toast.LENGTH_SHORT).show();
                finish();
               }
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder2=new AlertDialog.Builder(blog_composer.this);
                builder2.setTitle("Discard")
                        .setMessage("Really want to Discard?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                bloglist.clear();


//                                deleting statement is still an issue

                                new_blog.notifyDataSetChanged();
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog2=builder2.create();
                dialog2.show();
            }
        });




        heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(blog_composer.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.edit_profile_popup);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                LinearLayout drawer = dialog.findViewById(R.id.drawer);
                final TextView heading;
                final EditText editor;
                editor = dialog.findViewById(R.id.edited_text);
                heading=dialog.findViewById(R.id.heading);
                heading.setText("Enter your name");


                //editor.setText(name.getText().toString());

                Animation transintion = AnimationUtils.loadAnimation(blog_composer.this, R.anim.transition_upward);
                drawer.setAnimation(transintion);
                Button save=dialog.findViewById(R.id.save);
                Button cancel=dialog.findViewById(R.id.cancel);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        blogspan=new blog_list(editor.getText().toString(),"","","","",0);
                        bloglist.add(blogspan);
                        new_blog.notifyDataSetChanged();

                        dialog.dismiss();
                        emptyblog.setVisibility(View.GONE);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        subtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog2 = new Dialog(blog_composer.this);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.setContentView(R.layout.edit_profile_popup);
                dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                LinearLayout drawer2 = dialog2.findViewById(R.id.drawer);
                final TextView subtitle;
                final EditText editor2;
                editor2 = dialog2.findViewById(R.id.edited_text);
                subtitle=dialog2.findViewById(R.id.heading);
                subtitle.setText("Subtitle");


                //editor.setText(name.getText().toString());

                Animation transintion2 = AnimationUtils.loadAnimation(blog_composer.this, R.anim.transition_upward);
                drawer2.setAnimation(transintion2);
                Button save2=dialog2.findViewById(R.id.save);
                Button cancel2=dialog2.findViewById(R.id.cancel);
                save2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        blogspan=new blog_list("",editor2.getText().toString(),"","","",1);
                        bloglist.add(blogspan);
                        new_blog.notifyDataSetChanged();

                        dialog2.dismiss();
                        emptyblog.setVisibility(View.GONE);
                    }
                });

                cancel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });
                dialog2.show();

            }
        });


        para.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog3 = new Dialog(blog_composer.this);
                dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog3.setContentView(R.layout.edit_profile_popup);
                dialog3.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                LinearLayout drawer3 = dialog3.findViewById(R.id.drawer);
                final TextView para;
                final EditText editor3;
                editor3 = dialog3.findViewById(R.id.edited_text);
                para=dialog3.findViewById(R.id.heading);
                para.setText("Paragraph");


                //editor.setText(name.getText().toString());

                Animation transintion3 = AnimationUtils.loadAnimation(blog_composer.this, R.anim.transition_upward);
                drawer3.setAnimation(transintion3);
                Button save3=dialog3.findViewById(R.id.save);
                Button cancel3=dialog3.findViewById(R.id.cancel);
                save3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        blogspan=new blog_list("","",editor3.getText().toString(),"","",2);
                        bloglist.add(blogspan);
                        new_blog.notifyDataSetChanged();

                        dialog3.dismiss();
                        emptyblog.setVisibility(View.GONE);
                    }
                });

                cancel3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog3.dismiss();
                    }
                });
                dialog3.show();
            }
        });

        blogpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(blog_composer.this);


            }
        });

        hyperlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog4 = new Dialog(blog_composer.this);
                dialog4.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog4.setContentView(R.layout.edit_profile_popup);
                dialog4.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                LinearLayout drawer3 = dialog4.findViewById(R.id.drawer);
                final TextView link;
                final EditText editor4;
                editor4 = dialog4.findViewById(R.id.edited_text);
                link=dialog4.findViewById(R.id.heading);
                link.setText("Enter Link");


                //editor.setText(name.getText().toString());

                Animation transintion4 = AnimationUtils.loadAnimation(blog_composer.this, R.anim.transition_upward);
                drawer3.setAnimation(transintion4);
                Button save4=dialog4.findViewById(R.id.save);
                Button cancel4=dialog4.findViewById(R.id.cancel);
                save4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        blogspan=new blog_list("","","","",editor4.getText().toString(),4);
                        bloglist.add(blogspan);
                        new_blog.notifyDataSetChanged();

                        dialog4.dismiss();
                        emptyblog.setVisibility(View.GONE);
                    }
                });

                cancel4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog4.dismiss();
                    }
                });
                dialog4.show();

            }
        });



    }


    private void uploadimage() {
        if (resulturi != null) {
            progressbar.setVisibility(View.VISIBLE);

            final StorageReference fileReference = storageReference.child(key + "." + getFileExtension(resulturi));


            fileReference.putFile(resulturi)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 500);

                            fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri dowload = task.getResult();
                                    String downloadurl = dowload.toString();

                                    blogspan=new blog_list("","","",downloadurl,"",3);
                                    bloglist.add(blogspan);
                                    new_blog.notifyDataSetChanged();
                                    emptyblog.setVisibility(View.GONE);



                                }
                            });


                            Toast.makeText(blog_composer.this, "Uploading Complete", Toast.LENGTH_LONG).show();

                            progressbar.setVisibility(View.GONE);

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(blog_composer.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            progressbar.setVisibility(View.VISIBLE);
                        }
                    });


        } else {
            Toast.makeText(this, "Select profile pic", Toast.LENGTH_SHORT).show();
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST
                && data != null && data.getData() != null) {
            filePath = data.getData();

        }
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                resulturi= result.getUri();
               // Picasso.with(this).load(resulturi).into(imageView);
                uploadimage();
            }

        }

    }
}
