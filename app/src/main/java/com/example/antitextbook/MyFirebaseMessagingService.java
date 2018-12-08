package com.example.antitextbook;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;


//class extending FirebaseMessagingService
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //private String title;
    //private String body;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //if the message contains data payload
        //It is a map of custom keyvalues
        //we can read it easily
        if(remoteMessage.getData().size() > 0){
            //handle the data message here
        }

        //getting the title and the body
        String title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        String body = remoteMessage.getNotification().getBody();

        MyNotificationManager.getInstance(this).displayNotification(title, body);

        //title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        //body = remoteMessage.getNotification().getBody();

        //then here we can use the title and body to build a notification
    }

    /*public String getTitle(){
        return title;
    }

    public String getBody(){
        return body;
    }
*/

}