package com.example.artikproject.background_work.theme;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.artikproject.background_work.datebase.MySharedPreferences;


public class Theme {
    public static int current_theme;

    public static void setting(Activity act){
        current_theme = get(act.getApplicationContext());
        AppCompatDelegate.setDefaultNightMode(current_theme);
    }

    /**
     * Получить изменить тему приложения
     * @param context Контекст приложения
     * @param new_theme Id новой темы
     */
    public static void set(Context context, int new_theme){ // Установить тему
        MySharedPreferences.put(context, "currentTheme", new_theme);
    }

    /**
     * Получить текущую тему приложения
     * @param context Контекст приложения
     * @return Id сохраненной темы
     */
     public static int get(Context context){ // Получить тему и установить ее в системе
         return MySharedPreferences.get(context, "currentTheme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }
}
