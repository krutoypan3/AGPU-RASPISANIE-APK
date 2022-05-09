package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties;

import java.util.ArrayList;
import java.util.List;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems;
import ru.agpu.artikproject.background_work.site_parse.GetFullGroupList_Online;

public class LoadFacultiesList extends Thread{
    public static final List<RecyclerViewItems> FACULTIES_LIST = new ArrayList<>();
    /**
     * Подгружает список факультетов
     */
    public LoadFacultiesList(){}

    @Override
    public void run(){
        FACULTIES_LIST.clear();
        int SIZE_LIST = GetFullGroupList_Online.faculties_name.size();
        for(int i = 0; i < SIZE_LIST; i++){
            String current_item = GetFullGroupList_Online.faculties_name.get(i).item;
            int ico = R.drawable.agpu_ico;
            if(current_item.contains("Институт прикладной информатики"))
                ico = R.drawable.agpu_ico_ipimif;
            else if (current_item.contains("Институт русской"))
                ico = R.drawable.agpu_ico_iriif;
            else if (current_item.contains("Исторический факультет"))
                ico = R.drawable.agpu_ico_istfak;
            else if (current_item.contains("Социально-психологический факультет"))
                ico = R.drawable.agpu_ico_spf;
            else if (current_item.contains("Факультет дошкольного"))
                ico = R.drawable.agpu_ico_fdino;
            else if (current_item.contains("Факультет технологии"))
                ico = R.drawable.agpu_ico_fteid;
            FACULTIES_LIST.add(new RecyclerViewItems(GetFullGroupList_Online.faculties_name.get(i).item, ico, ""));
        }
    }
}
