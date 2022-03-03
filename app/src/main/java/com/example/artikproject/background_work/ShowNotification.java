package com.example.artikproject.background_work;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.artikproject.R;
import com.example.artikproject.layout.MainActivity;

public class ShowNotification extends AsyncTask<Void, Void, Void> {
    Context context;
    String title;
    String subtitle;

    public ShowNotification(Context context, String title, String subtitle) {
        this.context =context;
        this.title = title;
        this.subtitle = subtitle;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Void doInBackground(Void... voids) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int id = 123123; // The id of the channel. Ид канала
        CharSequence name = "agpu_chanel"; // Название канала которое будет видеть пользователь
        String description = "agpu_raspisanie"; // Описание канала (для пользователя)

        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel(id + "", name,importance);

        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        mNotificationManager.createNotificationChannel(mChannel);
        mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "123123"; // Тот же id канала только стринг
        // Вызов уведомления на канале

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(subtitle)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setChannelId(CHANNEL_ID)
                .setAutoCancel(true) // автоматически закрыть уведомление после нажатия
                .setContentIntent(contentIntent)
                .build();
        mNotificationManager.notify(id, notification);
        return null;
    }
}
