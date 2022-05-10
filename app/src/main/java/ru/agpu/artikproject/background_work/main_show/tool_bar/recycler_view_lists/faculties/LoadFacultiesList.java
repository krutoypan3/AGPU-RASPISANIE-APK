package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties;

import java.util.ArrayList;
import java.util.List;

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
            String ico = "https://i.ibb.co/PZhZjkZ/agpu-ico.png";
            if(current_item.contains("Институт прикладной информатики"))
                ico = "https://i.ibb.co/j33KDk0/agpu-ico-ipimif.png";
            else if (current_item.contains("Институт русской"))
                ico = "https://i.ibb.co/K6xBNY0/agpu-ico-iriif.png";
            else if (current_item.contains("Исторический факультет"))
                ico = "https://i.ibb.co/z42VmXz/agpu-ico-istfak.png";
            else if (current_item.contains("Социально-психологический факультет"))
                ico = "https://i.ibb.co/FzXkFdy/agpu-ico-spf.png";
            else if (current_item.contains("Факультет дошкольного"))
                ico = "https://i.ibb.co/hWQR2CL/agpu-ico-fdino.png";
            else if (current_item.contains("Факультет технологии"))
                ico = "https://i.ibb.co/gg2rsfV/agpu-ico-fteid.png";
            FACULTIES_LIST.add(new RecyclerViewItems(GetFullGroupList_Online.faculties_name.get(i).item, ico, ""));
        }
    }
}
