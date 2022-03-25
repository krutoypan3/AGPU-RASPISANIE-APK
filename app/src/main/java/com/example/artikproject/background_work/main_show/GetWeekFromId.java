package com.example.artikproject.background_work.main_show;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.artikproject.background_work.datebase.DataBase_Local;
import com.example.artikproject.layout.MainActivity;

public class GetWeekFromId {
    /**
     * Устанавливает текущую неделю на главном экране
     * @param act Активити
     */
    public GetWeekFromId(Activity act){
        try (SQLiteDatabase sqLiteDatabase = new DataBase_Local(act.getApplicationContext()).getWritableDatabase()) {
            Cursor f = sqLiteDatabase.rawQuery("SELECT week_s, week_po FROM weeks_list WHERE week_id = '" + MainActivity.week_id + "'", null);
            f.moveToFirst();
            String s_po = f.getString(0) + " " + f.getString(1);
            MainActivity.current_week.setText(s_po);
        }
    }
}
