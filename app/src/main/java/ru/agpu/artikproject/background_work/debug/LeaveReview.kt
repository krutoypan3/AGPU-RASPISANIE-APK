package ru.agpu.artikproject.background_work.debug

import android.content.Intent
import android.net.Uri
import android.view.View

/**
 * Класс отвечающий за переход на страницу разработчика для оставки отзыва
 * @param activity Контекст приложения
 */
class LeaveReview(view: View) {
    init {
        view.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/write267141542")))
    }
}