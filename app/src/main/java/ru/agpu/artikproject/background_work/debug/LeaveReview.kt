package ru.agpu.artikproject.background_work.debug

import android.app.Activity
import android.content.Intent
import android.net.Uri

/**
 * Класс отвечающий за переход на страницу разработчика для оставки отзыва
 * @param activity Контекст приложения
 */
class LeaveReview(activity: Activity) {
    init {
        activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/write267141542")))
    }
}