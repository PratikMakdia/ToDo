package com.example.to_do;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class SubTaskNotificationReciever extends BroadcastReceiver {
   private  static final String sub_channel_id="1002";
    @Override
    public void onReceive(Context context, Intent sub_intent) {
        int subnotificationId = sub_intent.getIntExtra("sub_notificationId",0 );
        String submessage = sub_intent.getStringExtra("sub_todo");

        // When notification is tapped, call MainActivity.
        Intent subIntent = new Intent(context, AddSubTaskActivity.class);
        PendingIntent subcontentIntent = PendingIntent.getActivity(context, 0, subIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager submyNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Prepare notification.
        Notification.Builder subbuilder = new Notification.Builder(context);
        subbuilder.setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("To Do Notification")
                .setContentText("Complete Your Sub Task "+submessage)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(subcontentIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel subchannel=new
                    NotificationChannel(sub_channel_id,"Hello",NotificationManager.IMPORTANCE_DEFAULT);
            subbuilder.setChannelId(sub_channel_id);
            if (submyNotificationManager != null) {
                submyNotificationManager.createNotificationChannel(subchannel);
            }
        }
        // Notify
        if (submyNotificationManager != null) {
            submyNotificationManager.notify(subnotificationId, subbuilder.build());
        }
    }
}
