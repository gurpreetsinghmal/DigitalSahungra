package com.sahungra.digitalsahungra;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;

public class VipAdapter extends RecyclerView.Adapter<VipAdapter.Panchviewholder> {
    private String[] name, desg, imgurl;
    Context ctx;
    String phone[] = new String[50];
    private static final int REQUEST_CALL = 1;
    String dial;
    public VipAdapter(Context ctx, String[] name, String[] desg, String[] imgurl) {
        this.name = name;
        this.desg = desg;
        this.imgurl = imgurl;
        this.ctx = ctx;
    }


    @NonNull
    @Override
    public Panchviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vip_call, parent, false);
        final Panchviewholder vholder = new Panchviewholder(view);
        vholder.vip_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makecall(phone[vholder.getAdapterPosition()]);
               // Toast.makeText(ctx, "Hey", Toast.LENGTH_SHORT).show();
            }


        });
        return vholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Panchviewholder holder, int position) {

        String mydesg = desg[position];
        holder.vip_desg.setText(mydesg);
        holder.vip_desg.setBackground(null);
        phone[position] = imgurl[position];


    }


    private void makecall(String id) {
        dial= "tel:" + id;

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) ctx, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            return;
         }
        else
        {
            ctx.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }


    }



    @Override
    public int getItemCount() {
        return name.length;
    }

    public class Panchviewholder extends RecyclerView.ViewHolder{
        LinearLayout vip_img;

        TextView vip_desg;

        public Panchviewholder(@NonNull View itemView) {
            super(itemView);
            vip_img = itemView.findViewById(R.id.ll);
            vip_desg = itemView.findViewById(R.id.vip_desg);

        }
    }


}
