package com.example.artikproject.background_work.rasp_show;

import static com.example.artikproject.layout.MainActivity.sqLiteDatabase;

import android.content.Context;
import android.database.Cursor;

import com.example.artikproject.background_work.CheckInternetConnection;
import com.example.artikproject.background_work.adapters.ListViewAdapter;
import com.example.artikproject.background_work.adapters.ListViewItems;
import com.example.artikproject.background_work.site_parse.GetRasp;
import com.example.artikproject.layout.MainActivity;
import com.example.artikproject.layout.Raspisanie_show;

import java.util.ArrayList;

public class Day_show {
    /**
     * Класс отвечающий за показ расписания в дневном режиме
     *
     * @param context Контекст приложения
     */
    public Day_show(Context context) {
        ArrayList<ListViewItems> group_list = new ArrayList<>();
        Cursor r = sqLiteDatabase.rawQuery("SELECT * FROM raspisanie WHERE " +
                "r_group_code = " + MainActivity.selectedItem_id + " AND " +
                "r_week_number = " + MainActivity.week_id + " AND " +
                "r_week_day = " + MainActivity.week_day + " ORDER BY r_para_number", null);
        if (r.getCount() != 0) {
            String str;
            r.moveToFirst();
            Raspisanie_show.mainText.setText((r.getString(10) + " " + r.getString(11)));
            do {
                str = "";
                if (r.getString(4) != null) {
                    str += r.getString(9) + "\n";
                    str += r.getString(4) + "\n";
                    if (r.getString(5) != null) str += r.getString(5) + "\n";
                    if (r.getString(6) != null) str += r.getString(6) + "\n";
                    if (r.getString(7) != null) str += r.getString(7) + "\n";
                    if (r.getString(8) != null) str += r.getString(8) + "\n";
                    if (r.getString(15) != null) str += r.getString(15) + "\n";
                    group_list.add(new ListViewItems(str));
                }
            } while (r.moveToNext());
        } else {
            if (CheckInternetConnection.getState(context)) {
                new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, context).start();
            }
        }
        ListViewAdapter adapter = new ListViewAdapter(context, group_list);
        Raspisanie_show.day_para_view.setAdapter(adapter);
    }
}

