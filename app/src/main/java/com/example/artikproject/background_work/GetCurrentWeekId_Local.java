package com.example.artikproject.background_work;

import static com.example.artikproject.layout.MainActivity.sqLiteDatabase;

import android.database.Cursor;

public class GetCurrentWeekId_Local {
    /**
     * Получение последнего номера недели сохраненного в локальной базе данных
     * @return Номер недели
     */
    public static int get(){
        try {
            Cursor r = sqLiteDatabase.rawQuery("SELECT value FROM settings_app WHERE settings_name = 'week_id'",null);
            r.moveToFirst();
            return Integer.parseInt(r.getString(0));
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
}
