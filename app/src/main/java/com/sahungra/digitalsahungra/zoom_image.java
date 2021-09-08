package com.sahungra.digitalsahungra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

public class zoom_image extends AppCompatActivity {
    ZoomageView image;
    Bundle bundle;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);


        image=findViewById(R.id.zoomimage);

            url=getIntent().getStringExtra("url");


        Picasso.with(zoom_image.this).load(url).into(image);


    }
}
