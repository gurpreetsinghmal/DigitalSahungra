package com.technominds.digitalsahungrav2;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper{


    public static void display_notification(Context ctx, String title, String body){
        PendingIntent pi = PendingIntent.getActivity(ctx, 0, new Intent(ctx, MainActivity.class), 0);

        NotificationCompat.Builder notification=new NotificationCompat.Builder(ctx,MainActivity.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .addAction(0, "View", pi);
        NotificationManagerCompat manager= NotificationManagerCompat.from(ctx);
        manager.notify(0,notification.build());
    }
}
