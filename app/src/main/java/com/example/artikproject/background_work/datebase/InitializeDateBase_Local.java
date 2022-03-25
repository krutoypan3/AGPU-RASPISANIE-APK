package com.example.artikproject.background_work.datebase;

import android.database.sqlite.SQLiteDatabase;

public class InitializeDateBase_Local {
    public InitializeDateBase_Local(SQLiteDatabase sqLiteDatabase){
        // Таблица с расписанием
        try {
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS \"rasp_test1\" (\n" +
                    "\t\"r_group_code\"\tINTEGER,\n" +
                    "\t\"r_week_day\"\tINTEGER,\n" +
                    "\t\"r_week_number\"\tINTEGER,\n" +
                    "\t\"r_para_number\"\tINTEGER,\n" +
                    "\t\"r_name\"\tTEXT,\n" +
                    "\t\"r_prepod\"\tTEXT,\n" +
                    "\t\"r_group\"\tTEXT,\n" +
                    "\t\"r_podgroup\"\tTEXT,\n" +
                    "\t\"r_aud\"\tTEXT,\n" +
                    "\t\"r_razmer\"\tINTEGER,\n" +
                    "\t\"r_week_day_name\"\tTEXT,\n" +
                    "\t\"r_week_day_date\"\tTEXT,\n" +
                    "\t\"r_search_type\"\tTEXT,\n" +
                    "\t\"r_last_update\"\tNUMERIC,\n" +
                    "\t\"r_color\"\tTEXT,\n" +
                    "\t\"r_distant\"\tTEXT\n" +
                    ")");
        }
        catch (Exception ignored){
        }
        // Таблица с избранным расписанием
        try {
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS \"rasp_update\" (\n" +
                    "\t\"r_group_code\"\tINTEGER,\n" +
                    "\t\"r_selectedItem_type\"\tTEXT,\n" +
                    "\t\"r_selectedItem\"\tTEXT\n" +
                    ")");
        }
        catch (Exception ignored){
        }
        // Таблица с настройками
        try {
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS \"settings_app\" (\n" +
                    "\t\"settings_name\"\tTEXT,\n" +
                    "\t\"value\"\tTEXT\n" +
                    ")");
//            ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
//            rowValues.put("settings_name", "theme_id");
//            rowValues.put("value", R.style.Theme_MyApp_Main);
//            sqLiteDatabase.insert("settings_app", null, rowValues);
        }
        catch (Exception ignored){
        }
        // Таблица с неделями
        try {
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS \"weeks_list\" (\n" +
                    "\t\"week_id\"\tTEXT,\n" +
                    "\t\"week_s\"\tTEXT,\n" +
                    "\t\"week_po\"\tTEXT\n" +
                    ")");
        }
        catch (Exception ignored){
        }
    }
}
