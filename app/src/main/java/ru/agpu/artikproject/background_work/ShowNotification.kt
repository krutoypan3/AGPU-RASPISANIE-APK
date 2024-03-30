package ru.agpu.artikproject.background_work

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import ru.agpu.artikproject.R
import ru.agpu.artikproject.presentation.layout.activity.MainActivity

/**
 * Класс отвечающий за показ уведомлений
 * @param context Контекст приложения
 * @param title Заголовок уведомления
 * @param subtitle Текст уведомления
 */
class ShowNotification(
    private val context: Context,
    private val title: String,
    private val subtitle: String,
    private val chanelId: Int
): Thread() {
    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun run() {
        var mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name: CharSequence = "agpu_chanel" // Название канала которое будет видеть пользователь
        val description = "agpu_raspisanie" // Описание канала (для пользователя)
        val importance = NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel(chanelId.toString() + "", name, importance)
        mChannel.description = description
        mChannel.enableLights(true)
        mChannel.lightColor = Color.BLUE
        mChannel.enableVibration(true)
        mChannel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
        mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        mNotificationManager.createNotificationChannel(mChannel)
        mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Вызов уведомления на канале
        val notificationIntent = Intent(context, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        val notification: Notification = Notification.Builder(context)
            .setContentTitle(title)
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentText(subtitle)
            .setSmallIcon(R.drawable.agpu_ico)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.agpu_ico)) // большая картинка
            .setChannelId(chanelId.toString())
            .setAutoCancel(true) // автоматически закрыть уведомление после нажатия
            .setContentIntent(contentIntent)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .build()
        mNotificationManager.notify(chanelId, notification)
    }
}
