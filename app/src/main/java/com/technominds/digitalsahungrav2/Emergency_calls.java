package com.technominds.digitalsahungrav2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Emergency_calls extends AppCompatActivity {
    RecyclerView recyclerView;
    VipAdapter adapter;


    LinearLayoutManager manager;
    View view;
    Context ctxx = null;
    int number_of_panch=50;
    LinearLayout progressbar;
    String[] name = new String[number_of_panch];
    String[] desg = new String[number_of_panch];
    String[] imgurl = new String[number_of_panch];
    private static final int REQUEST_CALL = 1;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_calls);
        progressbar=findViewById(R.id.progressbar);
        ctxx=Emergency_calls.this;

        recyclerView = findViewById(R.id.panch_recycle_postlist);

        Toolbar toolbar = findViewById(R.id.emergency_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.color2));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        manager = new LinearLayoutManager(ctxx);
        recyclerView.setLayoutManager(manager);

        recyclerView.hasFixedSize();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
    }

    private void loaddata(){

        progressbar.setVisibility(View.VISIBLE);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, BloggerApi.vip_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Log.d("response",response);
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray array=jsonObject.getJSONArray("vip");
                            for(int i=0;i<array.length();i++)
                            {   JSONObject o=array.getJSONObject(i);
                                name[i]=o.getString("type");
                                desg[i]=o.getString("type");
                                imgurl[i]=o.getString("detail");

                            }
                            List<String> list=new ArrayList<String>();
                            for(String s: name)
                            {
                                if(s!=null && s.length()>0)
                                {list.add(s);}
                            }
                            name=list.toArray(new String[list.size()]);
                            adapter = new VipAdapter(ctxx, name, desg, imgurl);
                            recyclerView.setAdapter(adapter);
                            //Toast.makeText(ctxx, response, Toast.LENGTH_SHORT).show();
                          progressbar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctxx, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue= Volley.newRequestQueue(Emergency_calls.this);
        requestQueue.add(stringRequest);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CALL&&grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {
            loaddata();

        }
        else{
            Toast.makeText(ctxx, "Permission for Call not Granted", Toast.LENGTH_SHORT).show();
        }
    }
}
