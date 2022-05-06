package ru.agpu.artikproject.background_work.service;

import android.content.Context;
import android.database.Cursor;


import ru.agpu.artikproject.background_work.datebase.DataBase_Local;
import ru.agpu.artikproject.background_work.site_parse.GetRasp;
import ru.agpu.artikproject.layout.MainActivity;
import ru.agpu.artikproject.background_work.CheckInternetConnection;
import ru.agpu.artikproject.background_work.GetCurrentWeekId_Local;

import java.util.ArrayList;

public class CheckRaspChanges {
    public CheckRaspChanges(Context context) {
        if (CheckInternetConnection.getState(context)) {
            MainActivity.sqLiteDatabase = new DataBase_Local(context).getWritableDatabase(); // Подключаемся к базе данных
            Cursor r = MainActivity.sqLiteDatabase.rawQuery("SELECT r_group_code, r_selectedItem_type, r_selectedItem FROM rasp_update", null); // SELECT запрос
            ArrayList<String> r_group0 = new ArrayList<>();
            ArrayList<String> r_group1 = new ArrayList<>();
            ArrayList<String> r_group2 = new ArrayList<>();

            int week_id_upd = GetCurrentWeekId_Local.get(context); // Номер текущей недели
            r.moveToFirst();
            do {
                if (r.getCount() != 0) {
                    r_group0.add(r.getString(0));
                    r_group1.add(r.getString(1));
                    r_group2.add(r.getString(2));
                }
            } while (r.moveToNext());

            for (int i = 0; i < r_group0.size(); i++) {
                new GetRasp(r_group0.get(i), r_group1.get(i), r_group2.get(i), week_id_upd, context, "CheckRaspChanges").start();
            }
        }

    }
}
