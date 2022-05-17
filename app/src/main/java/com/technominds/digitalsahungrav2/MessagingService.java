package com.technominds.digitalsahungrav2;

import android.media.RingtoneManager;
import android.net.Uri;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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

