package com.example.artikproject.background_work;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.artikproject.R;
import com.example.artikproject.layout.MainActivity;

import java.util.Random;

public class ShowNotification extends Thread {
    Context context;
    String title;
    String subtitle;
    int chanel_id = 12315;
    /**
     * Класс отвечающий за показ уведомлений
     * @param context Контекст приложения
     * @param title Заголовок уведомления
     * @param subtitle Текст уведомления
     */
    public ShowNotification(Context context, String title, String subtitle) {
        this.context =context;
        this.title = title;
        this.subtitle = subtitle;
    }
    public ShowNotification(Context context, String title, String subtitle, int chanel_id) {
        this.context =context;
        this.title = title;
        this.subtitle = subtitle;
        this.chanel_id = chanel_id;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
System.out.println(chanel_id + " ID КАНАЛА");
        CharSequence name = "agpu_chanel"; // Название канала которое будет видеть пользователь
        String description = "agpu_raspisanie"; // Описание канала (для пользователя)

        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel(chanel_id + "", name,importance);

        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        mChannel.enableVibration(true);
        mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        mNotificationManager.createNotificationChannel(mChannel);
        mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Вызов уведомления на канале

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(subtitle)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setChannelId(String.valueOf(chanel_id))
                .setAutoCancel(true) // автоматически закрыть уведомление после нажатия
                .setContentIntent(contentIntent)
                .build();
        mNotificationManager.notify(chanel_id, notification);
    }
}
