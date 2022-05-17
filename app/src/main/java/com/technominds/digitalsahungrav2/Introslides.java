package com.technominds.digitalsahungrav2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cuberto.liquid_swipe.LiquidPager;

public class Introslides extends AppCompatActivity {
    private LiquidPager viewPager;
    private int layouts[]={R.layout.intro_layout1, R.layout.intro_layout2, R.layout.intro_layout3};
    private MyViewpagerAdapter myViewpagerAdapter;
    private LinearLayout dots_layout;
    private ImageView[] dots;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introslides);

        if (new MySharedPref(this).checkPref()) {
            loadHome();
        }
        viewPager = findViewById(R.id.intro_viewpager);
        dots_layout = findViewById(R.id.ll_dots);
        myViewpagerAdapter = new MyViewpagerAdapter(layouts, this);
        viewPager.setAdapter(myViewpagerAdapter);
        create_dots(0);
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


    }

    private void loadHome()
    {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void create_dots(int position)
    {
        if(dots_layout!=null)
            dots_layout.removeAllViews();

        dots=new ImageView[layouts.length];
        for(int i=0;i<layouts.length;i++)
        {
            dots[i]=new ImageView(this);
            if(i==position)
            {
                dots[i].setImageDrawable(getDrawable(R.drawable.d_active));
            }
            else
            {
                dots[i].setImageDrawable(getDrawable(R.drawable.d_inactive));

            }
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            dots_layout.addView(dots[i],params);
        }

    }



    private void loadNextSlide()
    {
        int next_slide=viewPager.getCurrentItem()+1;
        if(next_slide<layouts.length)
        {
            viewPager.setCurrentItem(next_slide);
        }
        else
        {
            loadHome();
            new MySharedPref(this).writePref();
        }
    }
}
