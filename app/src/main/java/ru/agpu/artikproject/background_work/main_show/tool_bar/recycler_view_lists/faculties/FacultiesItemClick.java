package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewAdapter;
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties.groups.LoadFacultiesGroupsList;

public class FacultiesItemClick {
    /**
     * Обрабатывает нажатия на список факультетов
     * @param recyclerView RecyclerView
     * @param itemView Выбранный элемент (View)
     * @param datas Информация о выбранном элементе
     * @param act Активити
     */
    public FacultiesItemClick(RecyclerView recyclerView, View itemView, List<RecyclerViewItems> datas, Activity act){
        int itemPosition = recyclerView.getChildLayoutPosition(itemView); // Получаем позицию нажатого элемента
        RecyclerViewItems item  = datas.get(itemPosition); // Получаем сам нажатый элемент
        RecyclerViewAdapter.selected_faculties_position = itemPosition;
        RecyclerViewAdapter.selected_faculties_logos = item.getImageResourceUrl();
        recyclerView.setAdapter(new RecyclerViewAdapter(act, new LoadFacultiesGroupsList().get(itemPosition), RecyclerViewAdapter.IS_FACULTIES_GROUPS_ADAPTER));
    }
}
