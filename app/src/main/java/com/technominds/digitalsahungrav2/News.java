package com.technominds.digitalsahungrav2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class News extends Fragment {
    View view;
    RecyclerView newslist;
    LinearLayout progressbar;
    Context c=null;
    int count=50;
    String[] imgurl=new String[count];
    String[] title=new String[count];
    String[] date=new String[count];
    String[] source=new String[count];
    String[] content=new String[count];
    String[] link=new String[count];
    private int page_number=1;
    List<News_list> list;
    newsadapter newsadapter;


    String newsurl="https://newsapi.org/v2/top-headlines?country=in&apiKey=cc5d0f8310d645f89ca041e27b704a53";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Trending News");
        c=getContext();
        view = inflater.inflate(R.layout.fragment_news, container, false);
        newslist = view.findViewById(R.id.recycle_newslist);
        progressbar=view.findViewById(R.id.progressbar);
        newslist.setLayoutManager(new LinearLayoutManager(c));
        list=new ArrayList<>();

        fetchData();



        return view;



    }
    private void fetchData() {

        progressbar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, newsurl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {

                            list.clear();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);


                        JSONArray jsonArray = jsonObject.getJSONArray("articles");
                            int length = jsonArray.length();

                           // Toast.makeText(c, ""+length, Toast.LENGTH_SHORT).show();

                            for (int i = 0; i < length; i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                JSONObject s1 = jsonObject1.getJSONObject("source");

                                String source = s1.getString("name");

                                String url=jsonObject1.getString("urlToImage");
                                String title = jsonObject1.getString("title");
                                String link = jsonObject1.getString("url");



                                String desp = jsonObject1.getString("description");

                                String date = jsonObject1.getString("publishedAt");
                                String finaldate=date.substring(8,10)+"-"+date.substring(5,7)+"-"+date.substring(0,4);

                                News_list news_list=new News_list(title,desp,url,source,link,finaldate);
                                list.add(i,news_list);
                             }


                            newsadapter=new newsadapter(c,list);
                            newslist.setAdapter(newsadapter);
                            progressbar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put( "User-Agent", "Mozilla/5.0");

                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }
}



 class newsadapter extends RecyclerView.Adapter<newsadapter.ViewHolder> {

    private Context context;
    private List<News_list> uploads;

    public newsadapter(Context context, List<News_list> uploads) {
        this.uploads = uploads;
        this.context = context;

    }

    @Override
    public newsadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frag_news_item, parent, false);
        newsadapter.ViewHolder viewHolder = new newsadapter.ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final newsadapter.ViewHolder holder, int position) {
        final News_list list = uploads.get(position);
        holder.title.setText(list.getTitle());
        if(!list.getDesc().equalsIgnoreCase("null"))
        holder.des.setText(list.getDesc());

        Picasso.with(context).load(list.url).placeholder(context.getDrawable(R.mipmap.news)).into(holder.roundedImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(list.link_url));
                context.startActivity(i);
            }
        });
        holder.date.setText("Date : "+list.date);


    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView des;
        TextView date;
        RoundedImageView roundedImageView;


        public ViewHolder(final View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.news_title);
            des=itemView.findViewById(R.id.news_description);
            roundedImageView=itemView.findViewById(R.id.news_url);
            date=itemView.findViewById(R.id.news_date);


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




}
