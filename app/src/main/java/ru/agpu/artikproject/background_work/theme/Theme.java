package ru.agpu.artikproject.background_work.theme;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;


public class Theme {
    public static int current_theme;

    /**
     * Применяет сохраненную тему к приложению
     * @param act Активити
     */
    public static void setting(Activity act){
        current_theme = get(act.getApplicationContext());
        AppCompatDelegate.setDefaultNightMode(current_theme);
    }

    /**
     * Получить изменить тему приложения
     * @param context Контекст приложения
     * @param new_theme Id новой темы
     */
    public static void set(Context context, int new_theme){
        MySharedPreferences.INSTANCE.put(context, "currentTheme", new_theme);
    }

    /**
     * Получить сохраненную тему приложения
     * @param context Контекст приложения
     * @return Id сохраненной темы
     */
     public static int get(Context context){
         return MySharedPreferences.INSTANCE.get(context, "currentTheme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    /**
     * Получает тему приложения.
     * Если в приложении установлена тема системы, то получает системную тему.
     * @param context Контекст приложения
     * @return Id сохраненной темы
     */
    public static int getApplicationTheme(Context context){
         int theme = MySharedPreferences.INSTANCE.get(context, "currentTheme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
         if (theme == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
             theme = getCurrentSystemTheme(context);
         return theme;
    }

    /**
     * Получить текущую системную тему (светлая или темная). В неопределенном случае возвращает системную
     * @param context Контекст приложения
     * @return Id сохраненной темы
     */
    public static int getCurrentSystemTheme(Context context){
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                return AppCompatDelegate.MODE_NIGHT_NO;
            case Configuration.UI_MODE_NIGHT_YES:
                return AppCompatDelegate.MODE_NIGHT_YES;
            default:
                return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
    }
}
