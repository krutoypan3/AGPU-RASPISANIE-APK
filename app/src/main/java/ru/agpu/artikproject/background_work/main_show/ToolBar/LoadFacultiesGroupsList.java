package ru.agpu.artikproject.background_work.main_show.ToolBar;

import java.util.ArrayList;
import java.util.List;

import ru.agpu.artikproject.background_work.adapters.RecyclerView.RecyclerViewAdapter;
import ru.agpu.artikproject.background_work.adapters.RecyclerView.RecyclerViewItems;
import ru.agpu.artikproject.background_work.site_parse.GetFullGroupList_Online;

public class LoadFacultiesGroupsList {
    private final List<RecyclerViewItems> GROUPS_LIST = new ArrayList<>();

    public List<RecyclerViewItems> get(int itemPosition){
        int SIZE_LIST = GetFullGroupList_Online.faculties_group_name.get(itemPosition).size();
        for(int i = 0; i < SIZE_LIST; i++){
            GROUPS_LIST.add(new RecyclerViewItems(GetFullGroupList_Online.faculties_group_name.get(itemPosition).get(i).item, RecyclerViewAdapter.selected_faculties_logos, "test"));
        }
        return GROUPS_LIST;
    }
}
