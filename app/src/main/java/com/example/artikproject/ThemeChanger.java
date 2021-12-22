package com.example.artikproject;

import static com.example.artikproject.MainActivity.sqLiteDatabase;

import android.content.Context;
import android.database.ContentObservable;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeChanger {
    public int set() {
        Cursor r;
        r = sqLiteDatabase.rawQuery("SELECT DISTINCT value FROM settings_app WHERE settings_name = 'theme'", null);
        r.moveToNext();
        switch (r.getInt(0)){
            case(-1):
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case(0):
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case(1):
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
        return r.getInt(0);
    }
}
