package ru.agpu.artikproject.background_work.debug;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class LeaveReview{

    /**
     * Класс отвечающий за переход на страницу разработчика для оставки отзыва
     * @param activity Контекст приложения
     */
    public LeaveReview(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/write267141542"));
        activity.startActivity(intent);
    }
}

