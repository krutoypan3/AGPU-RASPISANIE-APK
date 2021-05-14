package com.example.artikproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class addNotification {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public addNotification(Context context, String title, String subtitle) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

// The id of the channel.
        int id = 123123;

// The user-visible name of the channel.
        CharSequence name = "agpu_chanel";

// The user-visible description of the channel.
        String description = "agpu_raspisanie";

        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel(id + "", name,importance);

// Configure the notification channel.
        mChannel.setDescription(description);

        mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);

        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        mNotificationManager.createNotificationChannel(mChannel);
        mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

// Sets an ID for the notification, so it can be updated.
        int notifyID = 1;

// The id of the channel.
        String CHANNEL_ID = "123123";

// Create a notification and set the notification channel.
        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(subtitle)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setChannelId(CHANNEL_ID)
                .build();

// Issue the notification.
        mNotificationManager.notify(id, notification);
    }

}
