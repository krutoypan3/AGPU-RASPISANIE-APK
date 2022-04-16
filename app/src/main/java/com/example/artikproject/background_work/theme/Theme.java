package com.example.artikproject.background_work.theme;

import static com.example.artikproject.layout.MainActivity.sqLiteDatabase;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatDelegate;


public class Theme {
    public static int current_theme;

    public static void setting(){
        current_theme = get();
        AppCompatDelegate.setDefaultNightMode(current_theme);
    }

    public static void set(int new_theme){ // Установить тему

        // Удаление старой темы из базы данных, если она там есть
        try{ sqLiteDatabase.delete("settings_app", "settings_name = 'theme_id'", null);}
        catch (Exception e){
            e.printStackTrace();
        }

        // Установка новой темы в базу данных
        ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
        rowValues.put("settings_name", "theme_id");
        rowValues.put("value", new_theme);
        sqLiteDatabase.insert("settings_app", null, rowValues);

        // Применение темы
        AppCompatDelegate.setDefaultNightMode(new_theme);
    }

     static int get(){ // Получить тему и установить ее в системе
        try{
            Cursor r = sqLiteDatabase.rawQuery("SELECT value FROM settings_app WHERE settings_name = 'theme_id'",null);
            r.moveToFirst();
            return Integer.parseInt(r.getString(0)); // Если тема есть в базе данных, то берем ее
        }
        catch (Exception e){
            return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM; // Иначе ставим как в системе
        }
    }
}
