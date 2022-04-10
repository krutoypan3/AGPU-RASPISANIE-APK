package com.example.artikproject.background_work.main_show;

import static com.example.artikproject.layout.MainActivity.sqLiteDatabase;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.artikproject.R;
import com.example.artikproject.background_work.datebase.DataBase_Local;
import com.example.artikproject.background_work.widget.MyWidgetConfig;
import com.example.artikproject.layout.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class WatchSaveGroupRasp {
    Cursor r;
    public List<String> group_list = new ArrayList<>();
    public List<String> group_list_type = new ArrayList<>();
    public List<String> group_list_id = new ArrayList<>();

    /**
     * Обновляет список ранее открываемых групп
     * @param context Контекст главного активити
     */
    public WatchSaveGroupRasp(Context context, boolean widget){
        if (widget){
            sqLiteDatabase = new DataBase_Local(context).getWritableDatabase();
            r = sqLiteDatabase.rawQuery("SELECT DISTINCT r_group_code, r_group, r_search_type, r_prepod, r_aud FROM raspisanie WHERE r_group NOT NULL AND r_prepod NOT NULL AND r_search_type NOT NULL GROUP BY r_group_code", null);
            if (r.moveToFirst()){
                watch();
            }
        }
    }

    /**
     * Выводит ранее открываемых групп
     * @param context Контекст главного активити
     */
    public WatchSaveGroupRasp(Context context){ // Вывод ранее открываемых групп
        r = sqLiteDatabase.rawQuery("SELECT DISTINCT r_group_code, r_group, r_search_type, r_prepod, r_aud FROM raspisanie WHERE r_group NOT NULL AND r_prepod NOT NULL AND r_search_type NOT NULL GROUP BY r_group_code", null);
        if (r.moveToFirst()){
            watch();
            MainActivity.group_listed = group_list.toArray(new String[0]);
            MainActivity.group_listed_type = group_list_type.toArray(new String[0]);
            MainActivity.group_listed_id = group_list_id.toArray(new String[0]);
        } // Вывод SELECT запроса
        if( MainActivity.group_listed == null || r.getCount() == 0){
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

    public void watch(){
        group_list = new ArrayList<>();
        group_list_type = new ArrayList<>();
        group_list_id = new ArrayList<>();
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
    }

}
