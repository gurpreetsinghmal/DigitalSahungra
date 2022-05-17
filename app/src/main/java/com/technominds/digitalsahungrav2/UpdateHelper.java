package com.technominds.digitalsahungrav2;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class UpdateHelper {
    public static String KEY_UPDTAE_ENABLE="isupdate";
    public static String KEY_UPDTAE_VERSION="version";
    public static String KEY_UPDTAE_URL="update_url";

    public interface OnUpdateCheckListner{
        void OnUpdateCheckListner(String urlApp);

    }

    private Context context;
    private OnUpdateCheckListner onUpdateCheckListner;

    public UpdateHelper(Context context, OnUpdateCheckListner onUpdateCheckListner) {
        this.context = context;
        this.onUpdateCheckListner = onUpdateCheckListner;
    }

    public static Builder with(Context context)
    {
        return new Builder(context);
    }

    public void check(){
        FirebaseRemoteConfig remoteConfig=FirebaseRemoteConfig.getInstance();
        if(remoteConfig.getBoolean(KEY_UPDTAE_ENABLE))
        {
            String current_version=remoteConfig.getString(KEY_UPDTAE_VERSION);
            String app_version=getAppVersion(context);
            String update_url=remoteConfig.getString(KEY_UPDTAE_URL);
            Toast.makeText(context, current_version+app_version, Toast.LENGTH_SHORT).show();
            if(!TextUtils.equals(current_version,app_version)&& onUpdateCheckListner!=null)
            {
                onUpdateCheckListner.OnUpdateCheckListner(update_url);
            }
        }
    }
    private String getAppVersion(Context context) {
        String result="";
        try{
            result=context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
            result=result.replaceAll("[a-zA-Z]|-","");
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static class Builder{
        private Context context;
        private OnUpdateCheckListner onUpdateCheckListner;



        public  Builder(Context context){
            this.context=context;

        }
        public Builder OnUpdateCheck(OnUpdateCheckListner onUpdateCheckListner)
        {
            this.onUpdateCheckListner=onUpdateCheckListner;
            return this;
        }

        public UpdateHelper build(){
            return new UpdateHelper(context,onUpdateCheckListner);
        }

        public UpdateHelper check(){
            UpdateHelper updateHelper=build();
            updateHelper.check();
            return updateHelper;
        }




    }
}
