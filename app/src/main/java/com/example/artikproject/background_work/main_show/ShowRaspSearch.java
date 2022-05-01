package com.example.artikproject.background_work.main_show;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.artikproject.R;
import com.example.artikproject.layout.MainActivity;

public class ShowRaspSearch {
    public ShowRaspSearch(Activity act){
        Button list_groups = act.findViewById(R.id.list_groups);
        ListView listview = act.findViewById(R.id.listview);
        ListView listview_aud = act.findViewById(R.id.listview_aud);
        Button list_weeks = act.findViewById(R.id.list_weeks);
        TextView result = act.findViewById(R.id.result);
        TextView today = act.findViewById(R.id.main_activity_text);
        TextView current_week = act.findViewById(R.id.subtitle);
        EditText rasp_search_edit = act.findViewById(R.id.rasp_search_edit);

        MainActivity.drawerResult.setSelection(0);

        listview.setVisibility(View.VISIBLE);
        result.setVisibility(View.VISIBLE);
        rasp_search_edit.setVisibility(View.VISIBLE);
        listview_aud.setVisibility(View.INVISIBLE);
        today.setVisibility(View.VISIBLE);
        current_week.setVisibility(View.VISIBLE);
        list_groups.setVisibility(View.VISIBLE);
        list_weeks.setVisibility(View.VISIBLE);
    }
}
