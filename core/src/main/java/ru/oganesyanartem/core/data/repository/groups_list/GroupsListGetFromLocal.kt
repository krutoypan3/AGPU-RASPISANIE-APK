package ru.oganesyanartem.core.data.repository.groups_list

import android.content.Context
import android.database.Cursor
import ru.oganesyanartem.core.data.database.sqlite.DataBase
import ru.oganesyanartem.core.domain.models.GroupsListItem
import java.lang.Exception

class GroupsListGetFromLocal {
    fun get(context: Context): List<GroupsListItem> {
        val sqLiteDatabase = DataBase(context = context).writableDatabase
        val groupsListItem = ArrayList<GroupsListItem>()
        try {
            val r: Cursor = sqLiteDatabase.rawQuery(
                "SELECT DISTINCT * FROM groups_list",
                null
            )
            while (r.moveToNext()) {
                groupsListItem.add(
                    GroupsListItem(
                        facultiesName = r.getString(0),
                        groupName = r.getString(1),
                        groupId = r.getString(2),
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            sqLiteDatabase.close()
        }
        return groupsListItem
    }
}