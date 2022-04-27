package com.example.artikproject.background_work.settings_layout;

import android.content.Context;
import android.widget.Toast;

import com.example.artikproject.R;
import com.example.artikproject.background_work.datebase.MySharedPreferences;

/**
 * Класс отвечающий за подсчет и добавление собранных пасхалок
 */
public class Ficha_achievements {
    public static final int MAX_FICHA_COUNT = 9;
    private static final String[] FICHA_LIST = new String[]{
            "ficha_toolbar1", "ficha_toolbar2", "ficha_setting_logo", "ficha_setting_leonardo",
            "ficha_para_lapshin", "ficha_para_kozlov", "ficha_refresh", "ficha_god",
            "ficha_today"};

    /**
     * Получить количество собранных пасхалок
     * @param context Контекст приложения
     */
    public static int get(Context context){
        int ficha_count = 0;
        for (String current_ficha : FICHA_LIST){
            if (MySharedPreferences.get(context, current_ficha, false)){
                ficha_count++;
            }
        }
        return ficha_count;
    }

    /**
     * Добавить собранную пасхалку
     * @param context Контекст приложения
     * @param name Пасхалка
     */
    public static void put(Context context, String name){
        if (!MySharedPreferences.get(context, name, false)){
            Toast.makeText(context, R.string.go_to_settings, Toast.LENGTH_SHORT).show();
        }
        MySharedPreferences.put(context, name, true);
    }
}
