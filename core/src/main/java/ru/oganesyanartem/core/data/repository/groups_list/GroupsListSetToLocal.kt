package ru.oganesyanartem.core.data.repository.groups_list

import android.content.ContentValues
import android.content.Context
import ru.oganesyanartem.core.data.database.sqlite.DataBase
import ru.oganesyanartem.core.domain.models.GroupsListItem

class GroupsListSetToLocal {
    fun set(context: Context, groupsListItems: List<GroupsListItem>) {
        val sqLiteDatabase = DataBase(context = context).writableDatabase
        val rowValues = ContentValues() // Значения для вставки в базу данных

        groupsListItems.forEach { item ->
            rowValues.put("faculties_name", item.facultiesName)
            rowValues.put("faculties_group_name", item.groupName)
            rowValues.put("faculties_group_id", item.groupId)
            sqLiteDatabase.insert("groups_list", null, rowValues)
        }
        sqLiteDatabase.close()
    }
}