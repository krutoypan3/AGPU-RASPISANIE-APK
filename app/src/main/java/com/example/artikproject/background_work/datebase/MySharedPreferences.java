package com.example.artikproject.background_work.datebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySharedPreferences {

    public static void put(Context context, String name, int value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(name, value);
        edit.apply(); //apply
    }

    public static void put(Context context, String name, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(name, value);
        edit.apply(); //apply
    }

    public static void put(Context context, String name, boolean value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(name, value);
        edit.apply(); //apply
    }

    public static void remove(Context context, String name){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(name);
        edit.apply(); //apply
    }

    public static String get(Context context, String name, String default_value){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(name, default_value);
    }

    public static int get(Context context, String name, int default_value){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(name, default_value);
    }
}
