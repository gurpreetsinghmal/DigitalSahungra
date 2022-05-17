package com.technominds.digitalsahungrav2.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.technominds.digitalsahungrav2.R;
import com.technominds.digitalsahungrav2.attachment_list;

import java.util.List;

public class attachment_adapter extends RecyclerView.Adapter< attachment_adapter.ViewHolder> {

    private Context context;
    private List<attachment_list> uploads;

    public attachment_adapter(Context context, List<attachment_list> uploads) {
        this.uploads = uploads;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attachment_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final attachment_list list = uploads.get(position);
        holder.title.setText(list.getFilename()+"."+list.getType());


        try {
            if(list.getType().equals("jpg") || list.getType().equals("png") ){
                holder.imageView.setImageResource(R.mipmap.file_icon);
            }else if(list.getType().equals("mp3")){
                holder.imageView.setImageResource(R.mipmap.mp3);
            }else if(list.getType().equals("pdf")){
                holder.imageView.setImageResource(R.mipmap.pdf);
            }else{
                holder.imageView.setImageResource(R.mipmap.file);
            }
        }catch (Exception e){}




    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        TextView title;
        ImageView imageView;


        public ViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.filename);
            imageView = itemView.findViewById(R.id.icon);

            this.item = itemView;

        }


    }
}

