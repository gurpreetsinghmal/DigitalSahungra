package com.technominds.digitalsahungrav2;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class Mymessagingservice extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData().size()>0){
            String title,message,url;
            title=remoteMessage.getData().get("title");
            message=remoteMessage.getData().get("message");
            url=remoteMessage.getData().get("img_url");



        }
        Log.d("remotex", "onMessageReceived: "+remoteMessage.getData());
        showNotification(remoteMessage,remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    public void showNotification(RemoteMessage rm,String title,String message){

        final NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"Mynotification")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.dslogo)
                .setAutoCancel(true)
                .setContentText(message);

        Picasso.with(getApplicationContext()).load(rm.getData().get("img_url")).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // loaded bitmap is here (bitmap)
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigLargeIcon(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) { }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        });










        NotificationManagerCompat manager= NotificationManagerCompat.from(this);
        manager.notify(999,builder.build());
    }
}
