package com.example.artikproject.background_work.theme;

import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.artikproject.R;


public class GetColorTextView {
    /**
     * Возвращает цвет текста для текущей темы приложения
     * @param context Контекст приложения
     * @return R.color.(цвет)
     */
    public static int getAppColor(Context context){
        if (Theme.current_theme == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM){
            return getSystemColor(context); // Если тема в приложении стоит как в системе
        }
        else{ // Если тема в приложении отличается от системной
            if (Theme.current_theme == AppCompatDelegate.MODE_NIGHT_YES){
                return context.getColor(R.color.gold); // Включена темная тема
            }
            else{
                return context.getColor(R.color.black); // Включена светлая тема
            }
        }
    }

    /**
     * Возвращает цвет текста для текущей темы системы
     * @param context Контекст приложения
     * @return R.color.(цвет)
     */
    public static int getSystemColor(Context context){
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) // Если текущая тема системы
            return context.getColor(R.color.black); // светлая
        else
            return context.getColor(R.color.gold); // темная
    }
}
