package ru.agpu.artikproject.background_work.main_show;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import ru.agpu.artikproject.background_work.CheckInternetConnection;
import ru.agpu.artikproject.background_work.adapters.RecyclerView.RecyclerViewAdapter;
import ru.agpu.artikproject.background_work.main_show.ToolBar.ShowRaspSearch;
import ru.agpu.artikproject.background_work.site_parse.GetFullGroupList_Online;
import ru.agpu.artikproject.background_work.site_parse.GetRasp;
import ru.agpu.artikproject.layout.MainActivity;
import ru.agpu.artikproject.layout.Raspisanie_show;

public class GroupsItemClick {
    public GroupsItemClick(RecyclerView recyclerView, View itemView, Activity act){
        int itemPosition = recyclerView.getChildLayoutPosition(itemView); // Получаем позицию нажатого элемента
        MainActivity.selectedItem = GetFullGroupList_Online.faculties_group_name.get(RecyclerViewAdapter.selected_faculties_position).get(itemPosition).item;
        MainActivity.selectedItem_type = "Group";
        MainActivity.selectedItem_id = GetFullGroupList_Online.faculties_group_id.get(RecyclerViewAdapter.selected_faculties_position).get(itemPosition).item;
        Intent intent = new Intent(act.getApplicationContext(), Raspisanie_show.class);
        if (CheckInternetConnection.getState(act.getApplicationContext())){
            new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, act.getApplicationContext()).start();
        }
        act.startActivity(intent);
        new ShowRaspSearch(act);
    }
}
