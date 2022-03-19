package com.example.artikproject.background_work.main_show;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.artikproject.R;
import com.example.artikproject.layout.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class WatchSaveGroupRasp {
    /**
     * Выводит ранее открываемых групп
     * @param context Контекст главного активити
     */
    public WatchSaveGroupRasp(Context context){ // Вывод ранее открываемых групп
        Cursor r;
        r = MainActivity.sqLiteDatabase.rawQuery("SELECT DISTINCT r_group_code, r_group, r_search_type, r_prepod, r_aud FROM rasp_test1 WHERE r_group NOT NULL AND r_prepod NOT NULL AND r_search_type NOT NULL GROUP BY r_group_code", null);
        if (r.moveToFirst()){
            List<String> group_list = new ArrayList<>();
            List<String> group_list_type = new ArrayList<>();
            List<String> group_list_id = new ArrayList<>();
            do{
                switch (r.getString(2)) {
                    case "Group":
                        group_list.add(r.getString(1).split(",")[0].replace(")","").replace("(", ""));
                        break;
                    case "Classroom":
                        group_list.add(r.getString(3).split(",")[2]);
                        break;
                    case "Teacher":
                        group_list.add(r.getString(3).split(",")[0]);
                        break;
                }
                group_list_type.add(r.getString(2));
                group_list_id.add(r.getString(0));
            }while(r.moveToNext());
            MainActivity.group_listed = group_list.toArray(new String[0]);
            MainActivity.group_listed_type = group_list_type.toArray(new String[0]);
            MainActivity.group_listed_id = group_list_id.toArray(new String[0]);
        } // Вывод SELECT запроса
        if( MainActivity.group_listed == null){
            MainActivity.result.setText(R.string.no_saved_group);
            MainActivity.listview.setVisibility(View.INVISIBLE);
        }
        else {
            MainActivity.listview.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter(context, R.layout.listviewadapterbl, MainActivity.group_listed);
            MainActivity.listview.setAdapter(adapter);
            MainActivity.result.setText("");
        }
    }
}
