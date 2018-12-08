package com.example.antitextbook;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.Objects;

import static java.security.AccessController.getContext;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {


    //this method will be called
    //when the token is generated
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        //now we will have the token
        String token = FirebaseInstanceId.getInstance().getToken();

        //for now we are displaying the token in the log
        //copy it as this method is called only when the new token is generated
        //and usually new token is only generated when the app is reinstalled or the data is cleared
        Log.d("MyRefreshedToken", token);

        // dYl-tLFhP3c:APA91bGJJ5EMH3xqyxai9hhoMCzmX4sdt5eFmPLyWlXgZdVV1P97NASCMMqGAzq72gcl2Lr8zHUYMRFxEyUGpWDXqCHY7LM9OxPRz4NZAmH7S_vFcAASwoHMgb4Gcgds6zQeg07zOeAD
    }
}
