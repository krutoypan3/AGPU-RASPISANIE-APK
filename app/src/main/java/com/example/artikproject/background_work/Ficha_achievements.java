package com.example.artikproject.background_work;

import android.content.Context;
import android.widget.Toast;

import com.example.artikproject.R;
import com.example.artikproject.background_work.datebase.MySharedPreferences;

public class Ficha_achievements {
    public static final int MAX_FICHA_COUNT = 8;

    public static int get(Context context){
        int ficha_count = 0;
        if (MySharedPreferences.get(context, "ficha_toolbar1", false)) ficha_count++;
        if (MySharedPreferences.get(context, "ficha_toolbar2", false)) ficha_count++;
        if (MySharedPreferences.get(context, "ficha_setting_logo", false)) ficha_count++;
        if (MySharedPreferences.get(context, "ficha_setting_leonardo", false)) ficha_count++;
        if (MySharedPreferences.get(context, "ficha_para_lapshin", false)) ficha_count++;
        if (MySharedPreferences.get(context, "ficha_para_kozlov", false)) ficha_count++;
        if (MySharedPreferences.get(context, "ficha_refresh", false)) ficha_count++;
        if (MySharedPreferences.get(context, "ficha_god", false)) ficha_count++;
        return ficha_count;
    }

    public static void put(Context context, String name){
        if (!MySharedPreferences.get(context, name, false)){
            Toast.makeText(context, R.string.go_to_settings, Toast.LENGTH_SHORT).show();
        }
        MySharedPreferences.put(context, name, true);
    }
}
