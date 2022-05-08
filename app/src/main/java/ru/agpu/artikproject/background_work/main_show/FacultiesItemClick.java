package ru.agpu.artikproject.background_work.main_show;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.agpu.artikproject.background_work.adapters.RecyclerView.RecyclerViewAdapter;
import ru.agpu.artikproject.background_work.adapters.RecyclerView.RecyclerViewItems;
import ru.agpu.artikproject.background_work.main_show.ToolBar.LoadFacultiesGroupsList;

public class FacultiesItemClick {
    public FacultiesItemClick(RecyclerView recyclerView, View itemView, List<RecyclerViewItems> datas, Activity act){
        int itemPosition = recyclerView.getChildLayoutPosition(itemView); // Получаем позицию нажатого элемента
        RecyclerViewItems item  = datas.get(itemPosition); // Получаем сам нажатый элемент
        RecyclerViewAdapter.selected_faculties_position = itemPosition;
        RecyclerViewAdapter.selected_faculties_logos = item.getImageResourceId();
        recyclerView.setAdapter(new RecyclerViewAdapter(act, new LoadFacultiesGroupsList().get(itemPosition), RecyclerViewAdapter.IS_FACULTIES_GROUPS_ADAPTER));
    }
}
