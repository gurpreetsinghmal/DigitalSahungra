package com.technominds.digitalsahungrav2.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.technominds.digitalsahungrav2.R;
import com.technominds.digitalsahungrav2.update;
import com.technominds.digitalsahungrav2.updates_detail;
import com.squareup.picasso.Picasso;

import java.util.List;

public class updates_adapter extends RecyclerView.Adapter< updates_adapter.ViewHolder> {

    private Context context;
    private List<update> uploads;

    public updates_adapter(Context context, List<update> uploads) {
        this.uploads = uploads;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.updates_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final update list = uploads.get(position);
        holder.title.setText(list.getTitle());
        holder.description.setText(list.getDescription());

        try {
            if(holder.imageView.getDrawable()==null)
                Picasso.with(context).load(list.getUrl().get(0)).fit().centerCrop().into(holder.imageView);
        }catch (Exception e){}


        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, updates_detail.class);
                i.putExtra("key",list.getKey());
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        TextView title;
        RoundedImageView imageView;
        TextView description;

        public ViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.post_title);
            imageView = itemView.findViewById(R.id.postimage);
            description=itemView.findViewById(R.id.post_description);


            this.item = itemView;

        }


    }
}

