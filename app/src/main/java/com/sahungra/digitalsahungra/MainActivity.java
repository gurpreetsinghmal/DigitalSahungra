package com.sahungra.digitalsahungra;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    LinearLayout profileactivity;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    String name,url;

    LinearLayout cancel_it;
    LinearLayout updatepopup;
    LinearLayout positivebutton;
    DatabaseReference databaseReference,databaseReference2;
    RoundedImageView profile;
    FirebaseAuth auth;
    int appversion;

    Intent i;
    FirebaseAuth mAuth;
    public static final String CHANNEL_ID="DS";
    public static final String CHANNEL_NAME="Digital Sahungra";
    public static final String CHANNEL_DESC="Digital Sahungra";
    public static final String NODE_REFERENCE="users";
    BottomNavigationView bottomNavigationView;
    recents_updates recents_updates;
    Dashboard dashboard;
    blogs blogs;
    News news;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 1);


        auth=FirebaseAuth.getInstance();
        profile=findViewById(R.id.profile);

        cancel_it=findViewById(R.id.cancel_it);
        updatepopup=findViewById(R.id.updatepopup);
        positivebutton=findViewById(R.id.positivebutton);
        databaseReference = FirebaseDatabase.getInstance().getReference("Userdata/");
        databaseReference2=FirebaseDatabase.getInstance().getReference("version");

        appversion= BuildConfig.VERSION_CODE;



        AnimationDrawable animationDrawable = (AnimationDrawable) positivebutton.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();






        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()&& !dataSnapshot.getValue(String.class).equals(appversion+"")){
                   updatepopup.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("My Notification","Mynotification",NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       // String msg = "";
//                        if (!task.isSuccessful()) {
//                            msg = "Failed";
//                        }

                       // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        profileactivity=findViewById(R.id.profileactivity);


        mAuth= FirebaseAuth.getInstance();
        //NotificationHelper.display_notification(this,"hi","hello");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel=new NotificationChannel(MainActivity.CHANNEL_ID,MainActivity.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(MainActivity.CHANNEL_DESC);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic("updates");
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
            if(task.isSuccessful())
            {
                String token=task.getResult().getToken();
                saveToken(token);
            }
            }
        });

        frameLayout=findViewById(R.id.fragment_container);

        setuptoolbar();

        bottomNavigationView=findViewById(R.id.botton_nav_bar);

        recents_updates=new recents_updates();
        dashboard=new Dashboard();
        news=new News();
        blogs=new blogs();



        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.home);
            getSupportFragmentManager().beginTransaction().replace(R.id.mycontainer,new Dashboard()).commit();
        }


        positivebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=com.sahungra.digitalsahungra");
                startActivity(new Intent(Intent.ACTION_VIEW, marketUri));

            }
        });

        cancel_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatepopup.setVisibility(View.GONE);
            }
        });








        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        setFragment(dashboard);
                        return true;
                    case R.id.updates:
                        setFragment(recents_updates);
                        return true;

                    case R.id.news:
                        setFragment(news);
                        return true;

                    case R.id.blogs:
                        setFragment(blogs);
                        return true;


                    default:
                        return false;
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, profile_editor.class);
                i.putExtra("name",name);
                i.putExtra("url",url);
                startActivity(i);
            }
        });


        toolbar.getOverflowIcon().setColorFilter(getResources().getColor(R.color.color2) , PorterDuff.Mode.SRC_ATOP);

        navigationView = findViewById(R.id.navigationview_menu);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                for(int i=getSupportFragmentManager().getBackStackEntryCount();i>1;i--)
                {
                    getSupportFragmentManager().popBackStack();
                }

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:


                        break;

                    case R.id.nav_office:
                        Intent lk=new Intent(MainActivity.this,official_cabin.class);
                        startActivity(lk);

                        break;

                    case R.id.star:
                        Intent l=new Intent(MainActivity.this,stars.class);
                        startActivity(l);

                        break;

                    case R.id.nav_members:
                       // Toast.makeText(MainActivity.this, "Cliked Members", Toast.LENGTH_SHORT).show();
                        Intent s=new Intent(MainActivity.this,panchayt_members.class);
                        startActivity(s);

                        break;
                    case R.id.nav_hospital:
                        // Toast.makeText(MainActivity.this, "Cliked Members", Toast.LENGTH_SHORT).show();
                      Intent i= new Intent(getApplicationContext(), MapsActivity.class);
                       startActivity(i);
                       break;
                    case R.id.nav_assets:
                        Intent k=new Intent(MainActivity.this,Assets.class);
                        startActivity(k);

                        break;

                    case R.id.nav_vip:
                         Intent j=new Intent(MainActivity.this,Emergency_calls.class);
                         startActivity(j);
                        break;
                    case R.id.saved_posts:
                        Intent h=new Intent(MainActivity.this,saved_posts.class);
                        startActivity(h);
                        break;
                    case R.id.donate:
                        Intent x=new Intent(MainActivity.this,donation.class);
                        startActivity(x);

                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().equals(auth.getCurrentUser().getPhoneNumber())) {
                        name=postSnapshot.child("name").getValue().toString();
                        url=postSnapshot.child("url").getValue().toString();
                        try {
                            if (profile.getDrawable() == null)
                                Picasso.with(MainActivity.this).load(url).fit().centerCrop().into(profile);
                        } catch (Exception e) {
                        }


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()==null) {
            Intent intent = new Intent(this, Auth_activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


    }


    private void saveToken(String token) {
        String email=mAuth.getCurrentUser().getEmail();
        User user=new User(email,token);
        DatabaseReference dbUser= FirebaseDatabase.getInstance().getReference(NODE_REFERENCE);
        dbUser.child(mAuth.getCurrentUser().getUid())
        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    //Toast.makeText(MainActivity.this, "Token Saved", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.contactus:
                i= new Intent(this, ReachLocation.class);
                startActivity(i);
            break;

            case R.id.complaints:
                i= new Intent(this, ComplaintActivity.class);
                startActivity(i);
                break;
            case R.id.logout:
                AlertDialog.Builder builder2=new AlertDialog.Builder(this);
                builder2.setTitle("Logout")
                        .setMessage("Really want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                Intent k=new Intent(MainActivity.this,Auth_activity.class);
                                startActivity(k);
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

                break;

        }
        return true;
    }

    public void shareapp(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        ApplicationInfo api=getApplicationContext().getApplicationInfo();
        String apkPath=api.sourceDir;

        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("application/vnd.android.package-archive");

        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkPath)));
        startActivity(Intent.createChooser(intent,"Share App using"));

    }

    @SuppressLint("ResourceAsColor")
    private void setuptoolbar() {
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.color2));



    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {

            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle("Exit")
                    .setMessage("Really want to Exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog2 = builder2.create();
            dialog2.show();

        }

    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.mycontainer,fragment);
        fragmentTransaction.commit();
    }



}

