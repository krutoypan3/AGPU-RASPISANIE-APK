package ru.agpu.artikproject.background_work.main_show;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems;
import ru.agpu.artikproject.background_work.datebase.DataBase_Local;
import ru.agpu.artikproject.layout.MainActivity;

public class WatchSaveGroupRasp {
    Cursor r;
    public ArrayList<ListViewItems> group_list = new ArrayList<>();
    public List<String> group_list_type = new ArrayList<>();
    public List<String> group_list_id = new ArrayList<>();
    /**
     * Обновляет список ранее открываемых групп
     * @param context Контекст главного активити
     */
    public WatchSaveGroupRasp(Context context, boolean widget){
        if (widget){
            DataBase_Local.sqLiteDatabase = new DataBase_Local(context).getWritableDatabase();
            r = DataBase_Local.sqLiteDatabase.rawQuery("SELECT DISTINCT r_group_code, r_group, r_search_type, r_prepod, r_aud FROM raspisanie WHERE r_group NOT NULL AND r_prepod NOT NULL AND r_search_type NOT NULL GROUP BY r_group_code", null);
            if (r.moveToFirst()){
                watch();
            }
        }
    }

    /**
     * Выводит ранее открываемых групп
     * @param act Активити
     */
    public WatchSaveGroupRasp(Activity act){ // Вывод ранее открываемых групп
        ListView listview = act.findViewById(R.id.listview);
        TextView result = act.findViewById(R.id.result);

        r = DataBase_Local.sqLiteDatabase.rawQuery("SELECT DISTINCT r_group_code, r_group, r_search_type, r_prepod, r_aud FROM raspisanie WHERE r_group NOT NULL AND r_prepod NOT NULL AND r_search_type NOT NULL GROUP BY r_group_code", null);
        if (r.moveToFirst()){
            watch();
            MainActivity.group_listed = group_list;
            MainActivity.group_listed_type = group_list_type.toArray(new String[0]);
            MainActivity.group_listed_id = group_list_id.toArray(new String[0]);
        } // Вывод SELECT запроса
        act.runOnUiThread(() -> {
            try {
                if (MainActivity.group_listed == null || r.getCount() == 0) {
                    result.setText(R.string.no_saved_group);
                } else {
                    ListViewAdapter adapter = new ListViewAdapter(act.getApplicationContext(), MainActivity.group_listed);
                    listview.setAdapter(adapter);
                    result.setText("");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public void watch(){
        group_list = new ArrayList<>();
        group_list_type = new ArrayList<>();
        group_list_id = new ArrayList<>();
        do{
            switch (r.getString(2)) {
                case "Group":
                    group_list.add(new ListViewItems(r.getString(1).split(",")[0].replace(")","").replace("(", "")));
                    break;
                case "Classroom":
                    group_list.add(new ListViewItems(r.getString(3).split(",")[r.getString(3).split(",").length - 1]));
                    break;
                case "Teacher":
                    group_list.add(new ListViewItems(r.getString(3).split(",")[0]));
                    break;
            }
            group_list_type.add(r.getString(2));
            group_list_id.add(r.getString(0));
        }while(r.moveToNext());
    }

}
