package ru.agpu.artikproject.background_work.main_show.tool_bar;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.layout.MainActivity;

public class ShowToolBarRecyclerView {
    /**
     * Показывает или скрывает список с тулбара или основную страницу поиска
     * @param act Активити
     */
    public ShowToolBarRecyclerView(Activity act, boolean show){
        ListView listview = act.findViewById(R.id.listview);
        RecyclerView recyclerView = act.findViewById(R.id.recyclerView);
        TextView result = act.findViewById(R.id.result);
        TextView today = act.findViewById(R.id.main_activity_text);
        TextView current_week = act.findViewById(R.id.subtitle);
        EditText rasp_search_edit = act.findViewById(R.id.rasp_search_edit);
        if (show) {
            listview.setVisibility(View.INVISIBLE);
            result.setVisibility(View.INVISIBLE);
            rasp_search_edit.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            today.setVisibility(View.INVISIBLE);
            current_week.setVisibility(View.INVISIBLE);
        }
        else{
            MainActivity.drawerResult.setSelection(0);
            listview.setVisibility(View.VISIBLE);
            result.setVisibility(View.VISIBLE);
            rasp_search_edit.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            today.setVisibility(View.VISIBLE);
            current_week.setVisibility(View.VISIBLE);
        }
    }
}
