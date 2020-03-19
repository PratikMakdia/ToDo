package com.example.to_do;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.example.to_do.model.TaskListAdapter;

/**
 * Created by sara on 2017/12/28.
 */

public class AlarmReceiver extends BroadcastReceiver {
    static final String channel_id="1001";

    @Override
    public void onReceive(Context context, Intent intent) {

        int notificationId = intent.getIntExtra("notificationId",0 );
        String message = intent.getStringExtra("todo");




        NotificationManager myNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("To Do Notification")
                .setContentText("Complete Your Task "+message)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
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

        Intent generatedNotificationId=new Intent(context, TaskListAdapter.class);
        generatedNotificationId.putExtra("GenerateNotificationId",notificationId);

        if (myNotificationManager != null) {
            myNotificationManager.notify(notificationId, builder.build());

            //int GenerateNotificationId= intent.getIntExtra("GenerateNotificationId",notificationId);
        }
    }


    public static void cancelNotification(Context ctx, int notifyId) {

        NotificationManager  myNotificationManager= (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (myNotificationManager != null) {
            myNotificationManager.cancel(notifyId);
        }
        else
        {
            Toast.makeText(ctx,"Null Manager",Toast.LENGTH_SHORT).show();
        }
    }
}
