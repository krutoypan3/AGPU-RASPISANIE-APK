package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties.groups

import android.content.Context
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewAdapter
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems
import ru.oganesyanartem.core.data.repository.groups_list.GroupsListImpl
import ru.oganesyanartem.core.domain.repository.GroupsListRepository
import ru.oganesyanartem.core.domain.usecase.groups_list.GroupsListGetByFacultiesUseCase

class LoadFacultiesGroupsList {
    /**
     * Подгружает список групп факультетов
     */
    operator fun get(context: Context?): List<RecyclerViewItems> {
        val groupsList: MutableList<RecyclerViewItems> = ArrayList()
        val groupsListRepository: GroupsListRepository = GroupsListImpl(context!!)
        val groupsListItems = GroupsListGetByFacultiesUseCase(
            groupsListRepository,
            RecyclerViewAdapter.selected_faculties_position
        ).execute()
        for (i in groupsListItems.indices) groupsList.add(
            RecyclerViewItems(
                groupsListItems[i].groupName,
                RecyclerViewAdapter.selected_faculties_logos,
                ""
            )
        )
        return groupsList
    }
}