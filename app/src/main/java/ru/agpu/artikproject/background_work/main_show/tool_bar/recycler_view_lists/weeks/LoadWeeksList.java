package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.weeks;

import java.util.ArrayList;
import java.util.List;

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
            String ico = "https://i.ibb.co/PZhZjkZ/agpu-ico.png";
            switch (current_month){
                case 9:
                case 10:
                case 11:
                    ico = "https://i.ibb.co/TK526m5/autumn-ico.png";
                    break;
                case 12:
                case 1:
                case 2:
                    ico = "https://i.ibb.co/5nSJcZ6/winter-ico.png";
                    break;
                case 3:
                case 4:
                case 5:
                    ico = "https://i.ibb.co/wBg0ZvJ/spring-ico.png";
                    break;
                case 6:
                case 7:
                case 8:
                    ico = "https://i.ibb.co/HNyPFSd/summer-ico.png";
                    break;
            }
            WEEKS_LIST.add(new RecyclerViewItems(item, ico, ""));
        }
    }
}
