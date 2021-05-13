    package com.example.artikproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "raspisanie.db";
    public static String LOG_TAG = "DATABASE";

    public DataBase(Context context) {

        super(context, DATABASE_NAME, null, 1);
        Log.d(LOG_TAG,"created database raspisanie");

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.d(LOG_TAG,"created table news in database rasp_test1");

        sqLiteDatabase.execSQL("CREATE TABLE \"rasp_test1\" (\n" +
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
                "\t\"r_last_update\"\tNUMERIC\n" +
                ")");
        sqLiteDatabase.execSQL("CREATE TABLE \"rasp_update\" (\n" +
                "\t\"r_group_code\"\tINTEGER,\n" +
                "\t\"r_selectedItem_type\"\tTEXT,\n" +
                "\t\"r_selectedItem\"\tTEXT\n" +
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
