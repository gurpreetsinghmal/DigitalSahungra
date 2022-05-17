package com.technominds.digitalsahungrav2;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class blog_Adapter extends RecyclerView.Adapter< blog_Adapter.ViewHolder> {

    private Context context;
    private List<blog_envelope> uploads;
    new_blog_timeline timeline;
    FirebaseAuth auth;
    List<Boolean> bool=new ArrayList<>();

    public blog_Adapter(Context context, List<blog_envelope> uploads) {
        this.uploads = uploads;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blog_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        for(int i=0;i<uploads.size();i++){
            bool.add(i,false);
        }

        auth=FirebaseAuth.getInstance();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final blog_envelope list = uploads.get(position);
        holder.name.setText(list.getName());
        Picasso.with(context).load(list.getDp()).into(holder.dp);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        holder.blog.setLayoutManager(linearLayoutManager);
        timeline=new new_blog_timeline(context, (List<blog_list>) list.getList());
        holder.blog.setAdapter(timeline);
        holder.timeago.setText(getTimeAgo(list.getDate()));



        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        if (holder.likes.getTag().equals("like")) {
                            FirebaseDatabase.getInstance().getReference().child("blogs").child(list.getKey()).child("likes").child(auth.getUid()).setValue(true);
                            FirebaseDatabase.getInstance().getReference().child("blogs").child(list.getKey()).child("dislikes").child(auth.getUid()).removeValue();
                        }

            }
        });

        holder.dislikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.dislikes.getTag().equals("dislike")) {
                    FirebaseDatabase.getInstance().getReference().child("blogs").child(list.getKey()).child("dislikes").child(auth.getUid()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("blogs").child(list.getKey()).child("likes").child(auth.getUid()).removeValue();
                }

            }
        });



        nrlikes(holder.nrliked,list.getKey());
        islikes(list.getKey(),holder.likes);

        nrdislikes(holder.nrdisliked,list.getKey());
        isdislikes(list.getKey(),holder.dislikes);



    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        TextView name;
        RoundedImageView dp;
        RecyclerView blog;
        TextView timeago;
        ImageButton likes,dislikes;
        TextView nrliked,nrdisliked;



        public ViewHolder(final View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.bloggername);
            dp=itemView.findViewById(R.id.bloggerdp);
            blog=itemView.findViewById(R.id.blog);
            dislikes=itemView.findViewById(R.id.dislike);
            nrdisliked=itemView.findViewById(R.id.nrdislikes);
            nrliked=itemView.findViewById(R.id.nrlikes);
            likes=itemView.findViewById(R.id.like);
            timeago=itemView.findViewById(R.id.timeago);

            this.item = itemView;

        }


    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getTimeAgo(Date date) {
        long time = date.getTime();
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = currentDate().getTime();
        if (time > now || time <= 0) {
            return "in the future";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "moments ago";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 60 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 2 * HOUR_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    private void islikes(String postid, final ImageButton imageView) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("blogs").child(postid).child("likes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                    imageView.getDrawable().setTint(context.getResources().getColor(R.color.blue));
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void nrlikes(final TextView likes, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("blogs").child(postid).child("likes");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()==1){
                    likes.setText(dataSnapshot.getChildrenCount() +"");
                }else{
                    likes.setText(dataSnapshot.getChildrenCount() +"");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void isdislikes(String postid, final ImageButton imageView) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("blogs").child(postid).child("dislikes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                    imageView.getDrawable().setTint(context.getResources().getColor(R.color.blue));
                    imageView.setTag("disliked");
                } else {
                    imageView.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                    imageView.setTag("dislike");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void nrdislikes(final TextView dislike, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("blogs").child(postid).child("dislikes");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()==1){
                    Log.d("dislike", "onDataChange: "+dataSnapshot.getChildrenCount()+"dislike");
                    dislike.setText(dataSnapshot.getChildrenCount()+"");
                }else{
                    dislike.setText(dataSnapshot.getChildrenCount()+"");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

