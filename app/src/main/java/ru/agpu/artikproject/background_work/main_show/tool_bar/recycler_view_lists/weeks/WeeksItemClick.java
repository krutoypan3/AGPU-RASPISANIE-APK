package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.weeks;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems;
import ru.agpu.artikproject.background_work.main_show.GetWeekFromId;
import ru.agpu.artikproject.background_work.main_show.tool_bar.ShowToolBarRecyclerView;
import ru.agpu.artikproject.background_work.site_parse.GetFullWeekList_Online;
import ru.agpu.artikproject.layout.MainActivity;

public class WeeksItemClick {
    /**
     * Обрабатывает нажатия на список недель
     * @param recyclerView RecyclerView
     * @param itemView Выбранный элемент (View)
     * @param datas Информация о выбранном элементе
     * @param act Активити
     */
    public WeeksItemClick(RecyclerView recyclerView, View itemView, List<RecyclerViewItems> datas, Activity act) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView); // Получаем позицию нажатого элемента
        RecyclerViewItems item  = datas.get(itemPosition); // Получаем сам нажатый элемент
        MainActivity.week_id = Integer.parseInt(GetFullWeekList_Online.weeks_id.get(itemPosition)); // Получаем id выбранной недели из списка
        MainActivity.week_day = 0; // Ставим понедельник
        new GetWeekFromId(act);
        new ShowToolBarRecyclerView(act, false);
        Toast.makeText(act.getApplicationContext(), act.getString(R.string.Selected_week) + " " + item.getMainText(), Toast.LENGTH_LONG).show();
    }
}
