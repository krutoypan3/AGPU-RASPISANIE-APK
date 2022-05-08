package ru.agpu.artikproject.background_work.main_show.ToolBar;

import java.util.ArrayList;
import java.util.List;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.adapters.RecyclerView.RecyclerViewItems;
import ru.agpu.artikproject.background_work.site_parse.GetCurrentWeekId;

public class LoadWeeksList{
    public static List<RecyclerViewItems> WEEKS_LIST = new ArrayList<>();

    public void Load(){
        int SIZE_LIST = GetCurrentWeekId.weeks_s_po.size();
        for(int i = 0; i < SIZE_LIST; i++){
            WEEKS_LIST.add(new RecyclerViewItems(GetCurrentWeekId.weeks_s_po.get(i).item, R.drawable.agpu_ico, "test"));
        }
    }
}
