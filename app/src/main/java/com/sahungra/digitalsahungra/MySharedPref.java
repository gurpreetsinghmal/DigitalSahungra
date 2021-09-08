package com.sahungra.digitalsahungra;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPref {

    private Context context;
    private SharedPreferences sharedPreferences;
    private String PREF_NAME="gog_init";
    private String PREF_KEY_NAME="gog_key";

    public MySharedPref(Context ctx)
    {
        this.context=ctx;
        getSharedPreferences();
    }

    public void getSharedPreferences()
    {
        sharedPreferences=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }

    public void writePref()
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(PREF_KEY_NAME,"OK");
        editor.commit();
    }

    public boolean checkPref()
    {
        boolean status=false;
        if(sharedPreferences.getString(PREF_KEY_NAME,"null").equals("null"))
        {
            status=false;
        }
        else
        {status=true;}
        return status;
    }

    public void clearPref()
    {
        sharedPreferences.edit().clear().commit();
    }


}
