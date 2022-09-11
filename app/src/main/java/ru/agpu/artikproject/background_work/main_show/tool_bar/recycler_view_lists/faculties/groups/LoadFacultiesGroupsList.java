package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties.groups;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewAdapter;
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems;
import ru.oganesyanartem.core.data.repository.groups_list.GroupsListImpl;
import ru.oganesyanartem.core.domain.models.GroupsListItem;
import ru.oganesyanartem.core.domain.repository.GroupsListRepository;
import ru.oganesyanartem.core.domain.usecase.groups_list.GroupsListGetByFacultiesUseCase;

public class LoadFacultiesGroupsList {

    /**
     * Подгружает список групп факультетов
     */
    public List<RecyclerViewItems> get(Context context) {
        List<RecyclerViewItems> GROUPS_LIST = new ArrayList<>();
        GroupsListRepository groupsListRepository = new GroupsListImpl(context);
        List<GroupsListItem> groupsListItems = new GroupsListGetByFacultiesUseCase(groupsListRepository, RecyclerViewAdapter.selected_faculties_position).execute();
        for (int i = 0; i < groupsListItems.size(); i++) {
            GROUPS_LIST.add(new RecyclerViewItems(groupsListItems.get(i).getGroupName(), RecyclerViewAdapter.selected_faculties_logos, ""));
        }
        return GROUPS_LIST;
    }
}
