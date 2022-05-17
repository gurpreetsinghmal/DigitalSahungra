package com.technominds.digitalsahungrav2;


import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class new_blog_timeline extends RecyclerView.Adapter {

    private Context context;
    private List<blog_list> uploads;

    public new_blog_timeline(Context context, List<blog_list> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {


      return uploads.get(position).getViewtype();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.blog_heading, parent, false);
            return new ViewHolder(view);
        } else  if (viewType == 1){

            view = layoutInflater.inflate(R.layout.blog_subtitle, parent, false);
            return new ViewHolderone(view);
        }else if(viewType==2){

            view = layoutInflater.inflate(R.layout.blog_para, parent, false);
            return new ViewHoldertwo(view);
        }else if(viewType==3){

            view = layoutInflater.inflate(R.layout.blog_image, parent, false);
            return new ViewHolderthree(view);
        }else{

            view = layoutInflater.inflate(R.layout.blog_link, parent, false);
            return new ViewHolderfour(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final blog_list list = uploads.get(position);

        if(list.getViewtype()==0){
            ViewHolder viewHolder=(ViewHolder) holder;
            viewHolder.title.setText(list.getHeading());
        }else  if(list.getViewtype()==1){
            ViewHolderone viewHolder=(ViewHolderone) holder;
            viewHolder.subtitle.setText(list.getSubtitle());
        }else  if(list.getViewtype()==2){
            ViewHoldertwo viewHolder=(ViewHoldertwo) holder;
            viewHolder.para.setText(list.getPara());
        }else if(list.getViewtype()==3){
            ViewHolderthree viewHolder= (ViewHolderthree) holder;
            Picasso.with(context).load(list.url).placeholder(R.drawable.profile).into(viewHolder.img);
        }else if(list.getViewtype()==4){
            ViewHolderfour viewHolder= (ViewHolderfour) holder;
            viewHolder.link.setText(list.getLink());
            viewHolder.link.setClickable(true);
            viewHolder.link.setMovementMethod(LinkMovementMethod.getInstance());
            String text = list.getLink();
            viewHolder.link.setText("Link: "+Html.fromHtml(text));
            viewHolder.link.setTextColor(context.getResources().getColor(R.color.blue));
        }
    }



    @Override
    public int getItemCount() {
        return uploads.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        TextView title;


        public ViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.blog_heading);
            this.item = itemView;

        }


    }

    class ViewHolderone extends RecyclerView.ViewHolder {
        View item;
        TextView subtitle;

        public ViewHolderone(final View itemView) {
            super(itemView);
            subtitle = itemView.findViewById(R.id.blog_Subtitle);

            this.item = itemView;

        }


    }

    class ViewHoldertwo extends RecyclerView.ViewHolder {
        View item;
        TextView para;

        public ViewHoldertwo(final View itemView) {
            super(itemView);
            para = itemView.findViewById(R.id.blog_para);

            this.item = itemView;

        }


    }

    class ViewHolderthree extends RecyclerView.ViewHolder {
        View item;
        ImageView img;

        public ViewHolderthree(final View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.blog_image);

            this.item = itemView;

        }


    }
    class ViewHolderfour extends RecyclerView.ViewHolder {
        View item;
        TextView link;

        public ViewHolderfour(final View itemView) {
            super(itemView);
            link = itemView.findViewById(R.id.blog_link);

            this.item = itemView;

        }


    }
}

