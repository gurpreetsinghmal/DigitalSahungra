package com.sahungra.digitalsahungra;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.sahungra.digitalsahungra.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URI;

public class MessagingService extends FirebaseMessagingService {
    public Uri alarmsound;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        alarmsound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if(remoteMessage.getNotification()!=null) {
            String title=remoteMessage.getNotification().getTitle();
            String body=remoteMessage.getNotification().getBody();
            NotificationHelper.display_notification(getApplicationContext(),title,body);
        }
    }


}

