package com.plotgen.rramirez.plotgenerator.Common;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.R;

public class MyNotificationManager {

    private Context mCtx;
    private static MyNotificationManager mInstance;

    private MyNotificationManager(Context context){
        mCtx=context;
    }

    public static synchronized MyNotificationManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new MyNotificationManager(context);
        }
        return mInstance;

    }

    public void displayNotification(String title, String body){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx,Constants.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);

        Intent intent  = new Intent(mCtx, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        if(mNotificationManager !=null){
            mNotificationManager.notify(1, mBuilder.build());
        }

    }

    public void displayNotification(String title, String body, String tag, String id) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx,Constants.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(body);

        Intent intent  = new Intent(mCtx, MainActivity.class);
        intent.putExtra("tag",tag);
        intent.putExtra("post_id",id);
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        if(mNotificationManager !=null){
            mNotificationManager.notify(1, mBuilder.build());
        }
    }
}
