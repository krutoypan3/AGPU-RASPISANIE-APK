package ru.agpu.artikproject.background_work.main_show.ToolBar;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.layout.MainActivity;

public class ShowRaspSearch {
    /**
     * Скрывает список корпусов и показывает основную страницу поиска
     * @param act Активити
     */
    public ShowRaspSearch(Activity act){
        Button list_groups = act.findViewById(R.id.list_groups);
        ListView listview = act.findViewById(R.id.listview);
        RecyclerView recyclerView = act.findViewById(R.id.recyclerView);
        Button list_weeks = act.findViewById(R.id.list_weeks);
        TextView result = act.findViewById(R.id.result);
        TextView today = act.findViewById(R.id.main_activity_text);
        TextView current_week = act.findViewById(R.id.subtitle);
        EditText rasp_search_edit = act.findViewById(R.id.rasp_search_edit);

        MainActivity.drawerResult.setSelection(0);

        listview.setVisibility(View.VISIBLE);
        result.setVisibility(View.VISIBLE);
        rasp_search_edit.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        today.setVisibility(View.VISIBLE);
        current_week.setVisibility(View.VISIBLE);
        list_groups.setVisibility(View.VISIBLE);
        list_weeks.setVisibility(View.VISIBLE);
    }
}
