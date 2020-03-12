package com.example.to_do;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by sara on 2017/12/28.
 */

public class AlarmReceiver extends BroadcastReceiver {
    static final String channel_id="1001";

    @Override
    public void onReceive(Context context, Intent intent) {

        // Get id & message from intent.
    /*    Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;*/



        int notificationId = intent.getIntExtra("notificationId",0 );
        String message = intent.getStringExtra("todo");

        // When notification is tapped, call MainActivity.
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager myNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Prepare notification.
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("To Do Notification")
                .setContentText("Complete Your Task "+message)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel=new
                    NotificationChannel(channel_id,"Hello",NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(channel_id);
            if (myNotificationManager != null) {
                myNotificationManager.createNotificationChannel(channel);
            }
        }
        // Notify
        if (myNotificationManager != null) {
            myNotificationManager.notify(notificationId, builder.build());
        }




    }
}
