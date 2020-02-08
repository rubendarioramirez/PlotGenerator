package com.plotgen.rramirez.plotgenerator.Common;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        Log.v("matilda", "Data is " + remoteMessage.getData().get("title"));

        if(Utils.getStringSharePref(getApplicationContext(),"notifications").equalsIgnoreCase("true")) {
        MyNotificationManager.getInstance(getApplicationContext())

                .displayNotification(title, body);

        }
    }

    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        if (refreshedToken != null && !refreshedToken.equals(""))
            Utils.saveOnSharePreg(getApplicationContext(), "firebase_token", refreshedToken);
        Log.d("This app", "Refreshed token: " + refreshedToken);

    }
}
