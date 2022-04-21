    package com.example.artikproject.background_work.datebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBase_Local extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "raspisanie.db";
    /**
     * Класс отвечающий за первичное создание \ подключение к локальной базе данных
     * @param context Контекст приложения
     */
    public DataBase_Local(Context context) {
        super(context, DATABASE_NAME, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createDB(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        createDB(db);
    }

    private void createDB(SQLiteDatabase db){
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS \"raspisanie\" (\n" +
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
        // База данных с избранным расписанием
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS \"rasp_update\" (\n" +
                    "\t\"r_group_code\"\tINTEGER,\n" +
                    "\t\"r_selectedItem_type\"\tTEXT,\n" +
                    "\t\"r_selectedItem\"\tTEXT\n" +
                    ")");
        }
        catch (Exception ignored){
        }
        // Таблица с неделями
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS \"weeks_list\" (\n" +
                    "\t\"week_id\"\tTEXT,\n" +
                    "\t\"week_s\"\tTEXT,\n" +
                    "\t\"week_po\"\tTEXT\n" +
                    ")");
        }
        catch (Exception ignored){
        }
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS \"groups_list\" (\n" +
                    "\t\"faculties_name\"\tTEXT,\n" +
                    "\t\"faculties_group_name\"\tTEXT,\n" +
                    "\t\"faculties_group_id\"\tTEXT\n" +
                    ")");
        }
        catch (Exception ignored){}
    }
}
