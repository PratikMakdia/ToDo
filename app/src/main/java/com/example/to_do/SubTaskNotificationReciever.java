package com.example.to_do;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class SubTaskNotificationReciever extends BroadcastReceiver {
    private static final String sub_channel_id = "1002";

    @Override
    public void onReceive(Context context, Intent sub_intent) {
        int subNotificationId = sub_intent.getIntExtra("sub_notificationId", 0);
        String subMessage = sub_intent.getStringExtra("sub_todo");


        NotificationManager subNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder subBuilder = new Notification.Builder(context);
        subBuilder.setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("To Do Notification")
                .setContentText("Complete Your Sub Task " + subMessage)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel subChannel = new
                    NotificationChannel(sub_channel_id, "Hello", NotificationManager.IMPORTANCE_DEFAULT);
            subBuilder.setChannelId(sub_channel_id);
            if (subNotificationManager != null) {
                subNotificationManager.createNotificationChannel(subChannel);
            }
        }
        // Notify
        if (subNotificationManager != null) {
            subNotificationManager.notify(subNotificationId, subBuilder.build());
        }
    }
}
