package com.sahungra.digitalsahungra;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        final FirebaseRemoteConfig remoteConfig=FirebaseRemoteConfig.getInstance();

        Map<String,Object> defaultvalue=new HashMap<>();
        defaultvalue.put(UpdateHelper.KEY_UPDTAE_ENABLE,false);
        defaultvalue.put(UpdateHelper.KEY_UPDTAE_VERSION,1.0);
        defaultvalue.put(UpdateHelper.KEY_UPDTAE_URL,"https://play.google.com/store/apps/details?id=com.sahungra.digitalsahungra");

        remoteConfig.setDefaults(defaultvalue);
        remoteConfig.fetch(1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    remoteConfig.activateFetched();
                }
            }
        });

    }
}
