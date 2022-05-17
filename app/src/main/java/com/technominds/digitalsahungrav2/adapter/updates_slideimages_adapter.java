package com.technominds.digitalsahungrav2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.makeramen.roundedimageview.RoundedImageView;
import com.technominds.digitalsahungrav2.R;
import com.technominds.digitalsahungrav2.zoom_image;
import com.squareup.picasso.Picasso;

import java.util.List;

public class updates_slideimages_adapter extends PagerAdapter {
    private List<String>models;
    private LayoutInflater layoutInflater;
    private Context context;

    public updates_slideimages_adapter(List<String> models, Context context) {
        this.models = models;
        this.context = context;
    }



    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.image_slider_layout_item,container,false);
        RoundedImageView imageView;
        View item=view.getRootView();
//        TextView title,desc;
        imageView=view.findViewById(R.id.iv_auto_image_slider);
//        title=view.findViewById(R.id.title);
//        desc=view.findViewById(R.id.desc);

        Picasso.with(context).load(models.get(position)).into(imageView);
//        title.setText(models.get(position).getTitle());
//        desc.setText(models.get(position).getDes());

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, zoom_image.class);
                i.putExtra("url",models.get(position));
                context.startActivity(i);
            }
        });

        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
