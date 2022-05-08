package ru.agpu.artikproject.background_work.main_show;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import ru.agpu.artikproject.background_work.main_show.ToolBar.ShowRaspSearch;
import ru.agpu.artikproject.background_work.site_parse.GetFullWeekList_Online;
import ru.agpu.artikproject.layout.MainActivity;

public class WeeksItemClick {
    // Функция обработки нажатия на элемент списка
    public WeeksItemClick(RecyclerView recyclerView, View itemView, Activity act) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView); // Получаем позицию нажатого элемента
        MainActivity.week_id = Integer.parseInt(GetFullWeekList_Online.weeks_id.get(itemPosition)); // Получаем id выбранной недели из списка
        MainActivity.week_day = 0; // Ставим понедельник
        new GetWeekFromId(act);
        new ShowRaspSearch(act);
    }
}
