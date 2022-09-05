package ru.agpu.artikproject.data.repository.groups_list

import android.content.Context
import ru.agpu.artikproject.domain.models.GroupsListItem
import ru.agpu.artikproject.domain.repository.GroupsListRepository

class GroupsListImpl(val context: Context): GroupsListRepository {
    override fun get(): List<GroupsListItem> {
        var groupsListItems = GroupsListGetFromLocal().get(context = context)
        if (groupsListItems.isEmpty()) {
            groupsListItems = GroupsListGetFromApi().get()
            if (groupsListItems.isNotEmpty()) {
                set(groupsListItems = groupsListItems)
            }
        }
        return groupsListItems
    }

    override fun getFaculties(): List<String> {
        return get().map { it.facultiesName }.toSet().toList()
    }

    private fun set(groupsListItems: List<GroupsListItem>) {
        GroupsListSetToLocal().set(context, groupsListItems = groupsListItems)
    }
}