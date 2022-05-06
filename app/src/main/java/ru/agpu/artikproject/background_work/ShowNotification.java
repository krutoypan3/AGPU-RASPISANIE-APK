package ru.agpu.artikproject.background_work;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import ru.agpu.artikproject.layout.MainActivity;
import ru.agpu.artikproject.R;

public class ShowNotification extends Thread {
    final Context context;
    final String title;
    final String subtitle;
    final int chanel_id;

    /**
     * Класс отвечающий за показ уведомлений
     * @param context Контекст приложения
     * @param title Заголовок уведомления
     * @param subtitle Текст уведомления
     */
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
        CharSequence name = "agpu_chanel"; // Название канала которое будет видеть пользователь
        String description = "agpu_raspisanie"; // Описание канала (для пользователя)

        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel(chanel_id + "", name,importance);

        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[] { 1000, 1000, 1000, 1000, 1000 });
        mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        mNotificationManager.createNotificationChannel(mChannel);
        mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Вызов уведомления на канале

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentText(subtitle)
                .setSmallIcon(R.drawable.agpu_ico)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.agpu_ico)) // большая картинка
                .setChannelId(String.valueOf(chanel_id))
                .setAutoCancel(true) // автоматически закрыть уведомление после нажатия
                .setContentIntent(contentIntent)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .build();
        mNotificationManager.notify(chanel_id, notification);
    }
}
