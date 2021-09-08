package com.sahungra.digitalsahungra;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class BloggerApi {
    public static final String b_key="AIzaSyDE1fZD6FVrU5ogm010EzFatUW_O0VMTD4";
    public static final String bn_key="AIzaSyB1Swy4gJvRYcNtgutdSjztjyvGB186lDY";
    public static final String key="AIzaSyCaga209SeuX6jezHYAbYukLgh3kldP7oA";
    public static final String direction_key="AIzaSyAUUoBl9DDFj_ayTIXDWwhl-TI4qMDYEO4";
    public static final String blog_id="7794676945748468454";
    public static final String blog_url="https://www.googleapis.com/blogger/v3/blogs/"+blog_id+"/posts/";
    public static final String panch_url="http://sahungra.org/app/panch.php";
    public static final String star_url="http://sahungra.org/app/star.php";
    public static final String static_url="http://sahungra.org/app/static.php";
    public static final String assets_url="http://sahungra.org/app/assets.php";
    public static final String complaint_url="http://sahungra.org/app/complaint_reg.php";
    public static final String upi_donation_url="http://sahungra.org/app/upi_donation.php";
    public static final String vip_url="http://sahungra.org/app/vip.php";
    public static final String slider_url="http://sahungra.org/app/slider.php";
    public static final String officials_url="7864683477545151176";
    public static final String stars_url="5313888807887350397";

    public static Post_service postservice=null;

    public static Post_service getService(){
        if(postservice==null)
        {
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(blog_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            postservice=retrofit.create(Post_service.class);

        }

        return postservice;
    }



    public interface Post_service
    {
        @GET
        Call<PostList> getPostList(@Url String blog_url);

       // @GET("{postId}/?key="+key)
      //  Call<Item> getPostbyId(@Path("postId") String id);
    }
}
