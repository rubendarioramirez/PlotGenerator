package com.plotgen.rramirez.plotgenerator;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        Gson gson = new GsonBuilder().serializeNulls().create();

        if(Utils.getSharePref(getApplicationContext(),"notifications").equalsIgnoreCase("true")) {
            if (remoteMessage.getData() != null) {
                if (remoteMessage.getData().get("tag").equalsIgnoreCase("post")) {
                    String tag = remoteMessage.getData().get("tag");
                    String id = remoteMessage.getData().get("id");
                    MyNotificationManager.getInstance(getApplicationContext())
                            .displayNotification(title, body, tag, id);
                    Log.e("remote message", remoteMessage.getData().toString());
                } else {
                    MyNotificationManager.getInstance(getApplicationContext())
                            .displayNotification(title, body);
                }
            } else {
                MyNotificationManager.getInstance(getApplicationContext())
                        .displayNotification(title, body);
            }
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
