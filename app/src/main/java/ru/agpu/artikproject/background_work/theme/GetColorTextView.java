package ru.agpu.artikproject.background_work.theme;

import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

import ru.agpu.artikproject.R;


public class GetColorTextView {
    /**
     * Возвращает цвет текста для текущей темы приложения
     * @param context Контекст приложения
     * @return R.color.(цвет)
     */
    public static int getAppColor(Context context){
        int theme = Theme.getApplicationTheme(context);
        if (theme == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM){
            return getSystemColor(context); // Если тема в приложении стоит как в системе (По-хорошему этот пункт срабатывать не должен)
        }
        else{ // Если тема в приложении отличается от системной
            if (theme == AppCompatDelegate.MODE_NIGHT_YES){
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
    private static int getSystemColor(Context context){
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) // Если текущая тема системы
            return context.getColor(R.color.black); // светлая
        else
            return context.getColor(R.color.gold); // темная
    }
}
