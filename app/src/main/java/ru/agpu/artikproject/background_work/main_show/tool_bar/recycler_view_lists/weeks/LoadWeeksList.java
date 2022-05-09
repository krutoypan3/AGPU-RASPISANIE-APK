package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.weeks;

import java.util.ArrayList;
import java.util.List;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems;
import ru.agpu.artikproject.background_work.site_parse.GetCurrentWeekId;

public class LoadWeeksList{
    public static final List<RecyclerViewItems> WEEKS_LIST = new ArrayList<>();
    /**
     * Подгружает список недель
     */
    public void Load(){
        WEEKS_LIST.clear();
        int SIZE_LIST = GetCurrentWeekId.weeks_s_po.size();
        for(int i = 0; i < SIZE_LIST; i++){
            String item = GetCurrentWeekId.weeks_s_po.get(i).item;
            int current_month = Integer.parseInt(item.split("\\.")[1]);
            int ico = R.drawable.agpu_ico;
            switch (current_month){
                case 9:
                case 10:
                case 11:
                    ico = R.drawable.autumn_ico;
                    break;
                case 12:
                case 1:
                case 2:
                    ico = R.drawable.winter_ico;
                    break;
                case 3:
                case 4:
                case 5:
                    ico = R.drawable.spring_ico;
                    break;
                case 6:
                case 7:
                case 8:
                    ico = R.drawable.summer_ico;
                    break;
            }
            WEEKS_LIST.add(new RecyclerViewItems(item, ico, ""));
        }
    }
}
