package com.example.antitextbook;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

/**
 * Вот это точно для получения push уведомлений
 */
//class extending FirebaseMessagingService
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //private String title;
    //private String body;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData().size() > 0){
        }

        //getting the title and the body
        String title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        String body = remoteMessage.getNotification().getBody();

        MyNotificationManager.getInstance(this).displayNotification(title, body);
    }

}