package com.sahungra.digitalsahungra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sahungra.digitalsahungra.adapter.comment_list_recycler_view_adapter;
import com.sahungra.digitalsahungra.adapter.updates_slideimages_adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class updates_detail extends AppCompatActivity {


    String key;
    DatabaseReference databaseReference;
//
    List<String> images;
    private int currentpage=-1;
    private LinearLayout dots_layout;
    private ImageView[] dots;
    EditText comment;
    comment_list_recycler_view_adapter commentsadapter;
    Button post;
    RecyclerView comment_recyclerview;
    List<comment_list> comments;
    ImageButton like;
    TextView nrlikes;
    ViewPager viewPager;
    FirebaseAuth auth;
    LinearLayout progressbar;
    update item;
    updates_slideimages_adapter adapter;
    String name;
    ImageButton share,save;

    TextView posttitle,postdescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updates_detail);
        key=getIntent().getStringExtra("key");
        dots_layout = findViewById(R.id.ll_dots);
        share=findViewById(R.id.ushare);
        progressbar=findViewById(R.id.progressbar);
        auth=FirebaseAuth.getInstance();
        save=findViewById(R.id.usave);
        comment=findViewById(R.id.comment_text);
        nrlikes=findViewById(R.id.postlikes);
        like=findViewById(R.id.ulike);

        post=findViewById(R.id.meripost);
        comment_recyclerview=findViewById(R.id.recycler_view_comment_box);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(updates_detail.this,RecyclerView.VERTICAL,false);
        comment_recyclerview.setLayoutManager(linearLayoutManager);
//
        viewPager = findViewById(R.id.viewpager);
        posttitle=findViewById(R.id.posttitle);
        postdescription=findViewById(R.id.postdescription);
        progressbar.setVisibility(View.VISIBLE);
//
//
//
//
        images = new ArrayList<>();
//
//
        databaseReference= FirebaseDatabase.getInstance().getReference();
//
//
        comments=new ArrayList<>();
        databaseReference.child("comments").child(key).addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    comment_list comment_list = postSnapshot.getValue(comment_list.class);
                    comments.add(0, comment_list);

                }
                 //creating adapter
               commentsadapter = new comment_list_recycler_view_adapter(updates_detail.this,comments);
                comment_recyclerview.setAdapter(commentsadapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//
//
        databaseReference.child("Userdata").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if(dataSnapshot1.getKey().equals(auth.getCurrentUser().getPhoneNumber())){
                        name = (dataSnapshot1.child("name").getValue().toString());

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("updates").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                images.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if(dataSnapshot1.getKey().equals(key)){
                        item=dataSnapshot1.getValue(update.class);

                    }

                }
                posttitle.setText(item.getTitle());
                postdescription.setText(item.getDescription());
                images=item.getUrl();
                if(item.getUrl().size()==1){
                    dots_layout.setVisibility(View.GONE);
                }else {
                    dots_layout.setVisibility(View.VISIBLE);
                create_dots(0);

                }

                adapter = new updates_slideimages_adapter(images, updates_detail.this);
                viewPager.setAdapter(adapter);
                viewPager.setClipToPadding(false);
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//
//
//
//
        final Handler handler=new Handler();
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(++currentpage,true);
                if(currentpage==images.size()-1){
                    currentpage=-1;
                }
            }
        };


        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }

        },500,4000);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                create_dots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//
//
//
//
//
//
//
//    }
//


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!comment.getText().toString().equals("")){
                    addcomments();
                }else {
                    Toast.makeText(updates_detail.this, "Comment box is empty.", Toast.LENGTH_SHORT).show();
                }


            }
        });
//
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             saveit();
             }
        });
//
//
        issaved();
//
//
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (like.getTag().equals("like")) {

                    FirebaseDatabase.getInstance().getReference().child("updates").child(key).child("likes").child(auth.getUid()).setValue(true);

                } else {
                    FirebaseDatabase.getInstance().getReference().child("updates").child(key).child("likes").child(auth.getUid()).removeValue();
                }
            }
        });
//
//
//
        islikes(key, like);
        nrlikes(nrlikes, key);
//
//
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, posttitle.getText().toString() + "\n\n" + images.get(0) +"\n\n"+postdescription.getText().toString());
                sendIntent.setType("text/plain");
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                v.getContext().startActivity(shareIntent);
            }
        });
//
//
//
   }
//
    private void addcomments() {



        final Date currenttime = Calendar.getInstance().getTime();
         comment_list comment_list = new comment_list(name, comment.getText().toString(), currenttime);
        databaseReference.child("comments").child(key).child(databaseReference.push().getKey()).setValue(comment_list);
        comment.setText("");

    }
//
    private void issaved(){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Userdata").child(auth.getCurrentUser().getPhoneNumber()).child("saved");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(key) && dataSnapshot.exists()){
                    save.setImageResource(R.drawable.ic_bookmark_black_24dp);
                }else {
                    save.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                  }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void saveit() {


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Userdata").child(auth.getCurrentUser().getPhoneNumber()).child("saved");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(key)){
                    save.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    reference.child(key).removeValue();
                }else {
                    save.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    reference.child(key).setValue(key);
                    Toast.makeText(updates_detail.this, "Saved to collection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//
//
//
    private void islikes(String postid, final ImageButton imageView) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("updates").child(postid).child("likes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_favorite_black_24dp);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void nrlikes(final TextView likes, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("updates").child(postid).child("likes");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()==1){
                    likes.setText(dataSnapshot.getChildrenCount() + " like");
                }else{
                    likes.setText(dataSnapshot.getChildrenCount() + " likes");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
//
//    @Override
//    public void onBackPressed() {
//        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.imagecontainer);
//        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
//        if(fragment!=null){
//            fragmentTransaction.remove(fragment);
//            fragmentTransaction.commit();
//        }else{
//        super.onBackPressed();}
//    }


    private void create_dots(final int position){
        if (dots_layout != null)
            dots_layout.removeAllViews();

        dots = new ImageView[4];
        for (int i = 0; i < 4; i++) {
            dots[i] = new ImageView(this);
            if (i == position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dots[i].setImageDrawable(getDrawable(R.drawable.d_active));

                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dots[i].setImageDrawable(getDrawable(R.drawable.d_inactive));
                }

            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            dots_layout.addView(dots[i], params);
        }
    }
}

